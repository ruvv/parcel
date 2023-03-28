package io.ruv.gateway.routes;

import io.ruv.gateway.service.filters.JwtAuthFilter;
import io.ruv.gateway.service.filters.RequireRoleFilter;
import io.ruv.userservice.api.UserRole;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class AdminRoutesConfig {

    @Bean
    public RouteLocator adminRoutes(RouteLocatorBuilder rlb,
                                       JwtAuthFilter jwtAuthFilter) {

        var roleFilter = new RequireRoleFilter(UserRole.ADMIN);

        return rlb.routes()

                .route("admin-customer-get", r -> r.path("/customers/{username}").and().method(HttpMethod.GET)
                        .filters(f -> f.filter(jwtAuthFilter)
                                .filter(roleFilter)
                                .setPath("/api/v1/customers/{username}"))
                        .uri("lb://user-service"))

                .route("admin-courier-get", r -> r.path("/couriers/{username}").and().method(HttpMethod.GET)
                        .filters(f -> f.filter(jwtAuthFilter)
                                .filter(roleFilter)
                                .setPath("/api/v1/couriers/{username}"))
                        .uri("lb://user-service"))

                .route("admin-courier-list-get", r -> r.path("/couriers").and().method(HttpMethod.GET)
                        .filters(f -> f.filter(jwtAuthFilter)
                                .filter(roleFilter)
                                .setPath("/api/v1/couriers"))
                        .uri("lb://user-service"))

                .route("admin-courier-create", r -> r.path("/couriers").and().method(HttpMethod.POST)
                        .filters(f -> f.filter(jwtAuthFilter)
                                .filter(roleFilter)
                                .setPath("/api/v1/couriers"))
                        .uri("lb://user-service"))
                .build();
    }
}
