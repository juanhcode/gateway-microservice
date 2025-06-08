package com.develop.gateway_microservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(exchange -> exchange.anyExchange().permitAll()) // Permite todas las solicitudes sin autenticación
                .csrf(csrf -> csrf.disable()); // Deshabilita CSRF (opcional en APIs REST)

        return http.build();
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("*"); // Orígenes permitidos
        corsConfig.addAllowedMethod("*"); // Métodos permitidos
        corsConfig.addAllowedHeader("*"); // Cabeceras permitidas
        corsConfig.setAllowCredentials(true); // Permitir credenciales

        org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig); // Aplicar configuración a todas las rutas

        return new CorsWebFilter(source);
    }

}
