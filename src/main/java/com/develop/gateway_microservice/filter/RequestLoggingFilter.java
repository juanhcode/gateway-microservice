package com.develop.gateway_microservice.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        long start = System.currentTimeMillis();

        return chain.filter(exchange).doFinally(signalType -> {
            long duration = System.currentTimeMillis() - start;
            int statusCode = Objects.requireNonNull(exchange.getResponse().getStatusCode()).value();
            String podName = System.getenv("HOSTNAME");
            log.info("[{}] [REQUEST] {} {} - Status: {} - Time: {}ms",
                    podName,
                    request.getMethod(),
                    request.getURI().getPath(),
                    statusCode,
                    duration);

        });
    }

    @Override
    public int getOrder() {
        return -1; // Muy alto en la cadena de filtros
    }
}
