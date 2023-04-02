package io.ruv.parcel.gateway.routes;

import io.ruv.parcel.gateway.service.filters.JwtAuthFilter;
import io.ruv.parcel.gateway.service.filters.RequireRoleFilter;
import io.ruv.parcel.user.api.UserRole;
import io.ruv.parcel.user.api.auth.ParcelCustomHeaders;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriComponentsBuilder;

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

                .route("courier-parcel-get", r -> r.path("/courier/parcels/{id}").and().method(HttpMethod.GET)
                        .filters(f -> f.filter(jwtAuthFilter)
                                .filter(roleFilter)
                                .rewritePath("/courier/parcels", "/api/v1/parcels"))
                        .uri("lb://parcel-service"))

                .route("courier-parcel-list", r -> r.path("/courier/parcels").and().method(HttpMethod.GET)
                        .filters(f -> f.filter(jwtAuthFilter)
                                .filter(roleFilter)
                                .rewritePath("/courier/parcels", "/api/v1/parcels")
                                .filter((exchange, chain) -> {

                                    var username = exchange.getRequest().getHeaders()
                                            .getOrEmpty(ParcelCustomHeaders.USERNAME).get(0);

                                    var modifiedUri = UriComponentsBuilder.fromUri(exchange.getRequest().getURI())
                                            .replaceQueryParam("courierUsername", username)
                                            .build(true).toUri();

                                    var mutated = exchange.getRequest().mutate()
                                            .uri(modifiedUri)
                                            .build();

                                    return chain.filter(exchange.mutate()
                                            .request(mutated)
                                            .build());
                                }))
                        .uri("lb://parcel-service"))

                .route("courier-parcel-status-change", r -> r.path("/courier/parcels/{id}/status").and().method(HttpMethod.PUT)
                        .filters(f -> f.filter(jwtAuthFilter)
                                .filter(roleFilter)
                                .setPath("/api/v1/parcels/{id}/status"))
                        .uri("lb://parcel-service"))

                .build();
    }
}
