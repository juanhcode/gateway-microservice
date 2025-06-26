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

    private static final Map<String, Map<String, List<String>>> roleAccessMap = Map.of(
            "Administrator", Map.of(
                    "GET", List.of(
                            "/products",
                            "/products/**",
                            "/orders/purchases/**",
                            "/payment-status/**",
                            "/notifications/**",
                            "/users/**",
                            "/auth/**",
                            "/deliveries/**",
                            "/orders/purchases"
                    ),
                    "POST", List.of(
                            "/products",
                            "/orders/purchases",
                            "/payment-status/**",
                            "/notifications/**",
                            "/users/**",
                            "/auth/**",
                            "/deliveries/**",
                            "/orders/purchases/create-test-payment",
                            "/orders/purchases/create-test-payment/.*"
                    ),
                    "PUT", List.of(
                            "/products/**",
                            "/orders/purchases/**",
                            "/payment-status/**",
                            "/notifications/**",
                            "/users/**",
                            "/auth/**",
                            "/deliveries/**"
                    ),
                    "DELETE", List.of(
                            "/products/**",
                            "/orders/purchases/**",
                            "/payment-status/**",
                            "/notifications/**",
                            "/users/**",
                            "/auth/**",
                            "/deliveries/**"
                    )
            ),
            "Delivery", Map.of(
                    "GET", List.of(
                            "/users/.*",
                            "/orders/purchases/.*/.*",
                            "/notifications/.*",
                            "/deliveries",
                            "/deliveries/status",
                            "/deliveries/.*",
                            "/orders/purchases",
                            "/payment-status/.*"
                    ),
                    "POST", List.of(
                            "/users/get-users"
                    ),
                    "PUT", List.of(
                            "/deliveries/.*",
                            "/orders/purchases/.*"
                    )
            ),
            "Customer", Map.of(
                    "GET", List.of(
                            "/users/.*",
                            "/products",
                            "/products/.*",
                            "/orders/purchases/.*",
                            "/orders/purchases/.*/.*",
                            "/notifications/.*",
                            "/payment-status/.*",
                            "/deliveries",
                            "/deliveries/status",
                            "/deliveries/.*"
                    ),
                    "POST", List.of(
                            "/orders/purchases",
                            "/users/get-users",
                            "/deliveries",
                            "/orders/purchases/create-test-payment",
                            "/orders/purchases/create-test-payment/.*"
                    ),
                    "PUT", List.of(
                            "/orders/purchases/.*",
                            "/notifications/.*"
                    )
            ),
            "default-role", Map.of(
                    "POST", List.of("/users/get-users")
            )
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().startsWith(uri));

    public boolean hasAccess(String role, String method, String path) {
        return roleAccessMap.getOrDefault(role, Map.of())
                .getOrDefault(method, List.of())
                .stream()
                .anyMatch(pattern -> path.matches(pattern.replace("**", ".*")));
    }
}
