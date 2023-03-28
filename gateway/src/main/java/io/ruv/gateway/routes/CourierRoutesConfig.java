package io.ruv.gateway.routes;

import io.ruv.gateway.service.ParcelCustomHeaders;
import io.ruv.gateway.service.filters.JwtAuthFilter;
import io.ruv.gateway.service.filters.RequireRoleFilter;
import io.ruv.userservice.api.UserRole;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class CourierRoutesConfig {

    @Bean
    public RouteLocator courierRoutes(RouteLocatorBuilder rlb,
                                      JwtAuthFilter jwtAuthFilter) {

        var roleFilter = new RequireRoleFilter(UserRole.COURIER);

        return rlb.routes()

                .route("courier-profile-get", r -> r.path("/courier").and().method(HttpMethod.GET)
                        .filters(f -> f.filter(jwtAuthFilter)
                                .filter(roleFilter)
                                .filter((exchange, chain) -> {

                                    var username = exchange.getRequest().getHeaders()
                                            .getOrEmpty(ParcelCustomHeaders.USERNAME).get(0);

                                    var mutated = exchange.getRequest().mutate()
                                            .path("/api/v1/couriers/" + username)
                                            .build();

                                    return chain.filter(exchange.mutate()
                                            .request(mutated)
                                            .build());
                                }))
                        .uri("lb://user-service"))

                .route("courier-profile-update", r -> r.path("/courier").and().method(HttpMethod.PUT)
                        .filters(f -> f.filter(jwtAuthFilter)
                                .filter(roleFilter)
                                .filter((exchange, chain) -> {

                                    var username = exchange.getRequest().getHeaders()
                                            .getOrEmpty(ParcelCustomHeaders.USERNAME).get(0);

                                    var mutated = exchange.getRequest().mutate()
                                            .path("/api/v1/couriers/" + username)
                                            .build();

                                    return chain.filter(exchange.mutate()
                                            .request(mutated)
                                            .build());
                                }))
                        .uri("lb://user-service"))

                .route("courier-status-update", r -> r.path("/courier/status").and().method(HttpMethod.PUT)
                        .filters(f -> f.filter(jwtAuthFilter)
                                .filter(roleFilter)
                                .filter((exchange, chain) -> {

                                    var username = exchange.getRequest().getHeaders()
                                            .getOrEmpty(ParcelCustomHeaders.USERNAME).get(0);

                                    var mutated = exchange.getRequest().mutate()
                                            .path("/api/v1/couriers/" + username + "/status")
                                            .build();

                                    return chain.filter(exchange.mutate()
                                            .request(mutated)
                                            .build());
                                }))
                        .uri("lb://user-service"))

                .route("courier-location-update", r -> r.path("/courier/location").and().method(HttpMethod.PUT)
                        .filters(f -> f.filter(jwtAuthFilter)
                                .filter(roleFilter)
                                .filter((exchange, chain) -> {

                                    var username = exchange.getRequest().getHeaders()
                                            .getOrEmpty(ParcelCustomHeaders.USERNAME).get(0);

                                    var mutated = exchange.getRequest().mutate()
                                            .path("/api/v1/couriers/" + username + "/location")
                                            .build();

                                    return chain.filter(exchange.mutate()
                                            .request(mutated)
                                            .build());
                                }))
                        .uri("lb://user-service"))
                .build();
    }
}
