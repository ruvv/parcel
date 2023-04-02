package io.ruv.parcel.gateway.routes;

import io.ruv.parcel.gateway.service.filters.JwtAuthFilter;
import io.ruv.parcel.gateway.service.filters.RequireRoleFilter;
import io.ruv.parcel.user.api.UserRole;
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

                .route("admin-parcel-get", r -> r.path("/parcels/{id}").and().method(HttpMethod.GET)
                        .filters(f -> f.filter(jwtAuthFilter)
                                .filter(roleFilter)
                                .setPath("/api/v1/parcels/{id}"))
                        .uri("lb://parcel-service"))

                .route("admin-parcel-list", r -> r.path("/parcels").and().method(HttpMethod.GET)
                        .filters(f -> f.filter(jwtAuthFilter)
                                .filter(roleFilter)
                                .setPath("/api/v1/parcels"))
                        .uri("lb://parcel-service"))

                .route("admin-parcel-status-change", r -> r.path("/parcels/{id}/status").and().method(HttpMethod.PUT)
                        .filters(f -> f.filter(jwtAuthFilter)
                                .filter(roleFilter)
                                .setPath("/api/v1/parcels/{id}/status"))
                        .uri("lb://parcel-service"))

                .route("admin-parcel-assign", r -> r.path("/parcels/{id}/assignee").and().method(HttpMethod.PUT)
                        .filters(f -> f.filter(jwtAuthFilter)
                                .filter(roleFilter)
                                .setPath("/api/v1/parcels/{id}/assignee"))
                        .uri("lb://parcel-service"))
                .build();
    }
}
