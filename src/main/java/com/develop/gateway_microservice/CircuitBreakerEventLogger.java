package com.develop.gateway_microservice;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CircuitBreakerEventLogger {

    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public CircuitBreakerEventLogger(CircuitBreakerRegistry circuitBreakerRegistry) {
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    @PostConstruct
    public void registerEventConsumers() {
        circuitBreakerRegistry.getAllCircuitBreakers().forEach(cb -> {
            cb.getEventPublisher()
                    .onStateTransition(event -> log.warn("[CB] {}: {} -> {}",
                            cb.getName(),
                            event.getStateTransition().getFromState(),
                            event.getStateTransition().getToState()))
                    .onError(event -> log.error("[CB] {}: Error: {}",
                            cb.getName(),
                            event.getThrowable().toString()))
                    .onCallNotPermitted(event -> log.warn("[CB] {}: Call not permitted (OPEN)",
                            cb.getName()));
        });
    }
}
