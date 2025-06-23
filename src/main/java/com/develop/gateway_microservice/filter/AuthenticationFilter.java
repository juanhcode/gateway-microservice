package com.develop.gateway_microservice.filter;

import com.develop.gateway_microservice.util.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator routeValidator;

    @Autowired
    private JWTUtils jwtUtils;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    log.warn("[AUTH] Faltante header de autorización");
                    return onError(exchange, "Authorization header is missing", 401);
                }
                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                try {
                    jwtUtils.validateToken(authHeader);
                    String userRole = jwtUtils.extractRole(authHeader);
                    String path = exchange.getRequest().getURI().getPath();

                    // Validar si el rol tiene acceso a la ruta
                    if (!routeValidator.hasAccess(userRole, path)) {
                        log.warn("[AUTH] Acceso denegado para el rol: {}", userRole);
                        return onError(exchange, "Access denied for role: " + userRole, 403);
                    }
                } catch (Exception e) {
                    log.warn("[AUTH] Token inválido o expirado: {}", e.getMessage());
                    return onError(exchange, "Token is invalid or expired", 403);
                }
            }
            return chain.filter(exchange);
        });
    }
    private Mono<Void> onError(ServerWebExchange exchange, String error, int httpStatus) {
        exchange.getResponse().setStatusCode(HttpStatus.valueOf(httpStatus));
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(("{\"error\": \"" + error + "\"}").getBytes());
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    public static class Config {
    }
}