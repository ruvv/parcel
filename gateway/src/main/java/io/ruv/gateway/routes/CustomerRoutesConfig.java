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
public class CustomerRoutesConfig {

    @Bean
    public RouteLocator customerRoutes(RouteLocatorBuilder rlb,
                                       JwtAuthFilter jwtAuthFilter) {

        var roleFilter = new RequireRoleFilter(UserRole.CUSTOMER);

        return rlb.routes()

                .route("customer-profile-get", r -> r.path("/customer").and().method(HttpMethod.GET)
                        .filters(f -> f.filter(jwtAuthFilter)
                                .filter(roleFilter)
                                .filter((exchange, chain) -> {

                                    var username = exchange.getRequest().getHeaders()
                                            .getOrEmpty(ParcelCustomHeaders.USERNAME).get(0);

                                    exchange.getRequest().mutate()
                                            .path("/api/v1/customers/" + username)
                                            .build();

                                    return chain.filter(exchange);
                                }))
                        .uri("lb://user-service"))

                .route("customer-profile-update", r -> r.path("/customer").and().method(HttpMethod.PUT)
                        .filters(f -> f.filter(jwtAuthFilter)
                                .filter(roleFilter)
                                .filter((exchange, chain) -> {

                                    var username = exchange.getRequest().getHeaders()
                                            .getOrEmpty(ParcelCustomHeaders.USERNAME).get(0);

                                    exchange.getRequest().mutate()
                                            .path("/api/v1/customers/" + username)
                                            .build();

                                    return chain.filter(exchange);
                                }))
                        .uri("lb://user-service"))
                .build();
    }
}
