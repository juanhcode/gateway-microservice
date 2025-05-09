package com.develop.gateway_microservice.filter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/users")
    public Mono<ResponseEntity<String>> userFallback() {
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("El servicio de usuarios no está disponible actualmente."));
    }

    @GetMapping("/auth")
    public Mono<ResponseEntity<String>> authFallback() {
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("El servicio de autenticación no está disponible actualmente."));
    }

    @GetMapping("/notifications")
    public Mono<ResponseEntity<String>> notificationsFallback() {
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("El servicio de notificaciones no está disponible actualmente."));
    }
}


