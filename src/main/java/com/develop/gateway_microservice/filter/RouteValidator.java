package com.develop.gateway_microservice.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    public static final List<String> openApiEndpoints = List.of(
            "/auth/register",
            "/auth/login",
            "/eureka",
            "/users/create"
    );

    private static final Map<String, List<String>> roleAccessMap = Map.of(
            "Administrator", List.of("/users/**", "/orders/**", "/payment-status/**"),
            "Delivery", List.of("/orders/**", "/payment-status/**"),
            "Customer", List.of("/orders/**", "/payment-status/**"),
            "default-role", List.of("/users/get-users")
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().startsWith(uri));

    public boolean hasAccess(String role, String path) {
        return roleAccessMap.getOrDefault(role, List.of())
                .stream()
                .anyMatch(pattern -> path.matches(pattern.replace("**", ".*")));
    }
}
