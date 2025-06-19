package com.develop.gateway_microservice.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/fallback")
@Slf4j
public class FallbackController {

    @GetMapping("/users")
    public Mono<ResponseEntity<String>> userFallback(ServerHttpRequest request) {
        log.warn("[FALLBACK] Servicio de usuarios no disponible. URI: {} - Método: {} - Headers: {}",
                request.getURI(), request.getMethod(), request.getHeaders());
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("El servicio de usuarios no está disponible actualmente."));
    }

    @GetMapping("/auth")
    public Mono<ResponseEntity<String>> authFallback(ServerHttpRequest request) {
        log.warn("[FALLBACK] Servicio de autenticación no disponible. URI: {} - Método: {} - Headers: {}",
                request.getURI(), request.getMethod(), request.getHeaders());
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("El servicio de autenticación no está disponible actualmente."));
    }

    @GetMapping("/notifications")
    public Mono<ResponseEntity<String>> notificationsFallback( ServerHttpRequest request) {
        log.warn("[FALLBACK] Servicio de notificaciones no disponible. URI: {} - Método: {} - Headers: {}",
                request.getURI(), request.getMethod(), request.getHeaders());
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("El servicio de notificaciones no está disponible actualmente."));
    }
}


