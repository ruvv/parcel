package io.ruv.parcel.gateway.routes;

import io.ruv.parcel.gateway.service.filters.JwtAuthFilter;
import io.ruv.parcel.gateway.service.filters.RequireRoleFilter;
import io.ruv.parcel.parcel.api.ParcelStatus;
import io.ruv.parcel.user.api.UserRole;
import io.ruv.parcel.user.api.auth.ParcelCustomHeaders;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriTemplate;
import reactor.core.publisher.Mono;

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

                                    var mutated = exchange.getRequest().mutate()
                                            .path("/api/v1/customers/" + username)
                                            .build();

                                    return chain.filter(exchange.mutate()
                                            .request(mutated)
                                            .build());
                                }))
                        .uri("lb://user-service"))

                .route("customer-profile-update", r -> r.path("/customer").and().method(HttpMethod.PUT)
                        .filters(f -> f.filter(jwtAuthFilter)
                                .filter(roleFilter)
                                .filter((exchange, chain) -> {

                                    var username = exchange.getRequest().getHeaders()
                                            .getOrEmpty(ParcelCustomHeaders.USERNAME).get(0);

                                    var mutated = exchange.getRequest().mutate()
                                            .path("/api/v1/customers/" + username)
                                            .build();

                                    return chain.filter(exchange.mutate()
                                            .request(mutated)
                                            .build());
                                }))
                        .uri("lb://user-service"))

                .route("customer-parcel-get", r -> r.path("/customer/parcels/{id}").and().method(HttpMethod.GET)
                        .filters(f -> f.filter(jwtAuthFilter)
                                .filter(roleFilter)
                                .rewritePath("/customer/parcels", "/api/v1/parcels"))
                        .uri("lb://parcel-service"))

                .route("customer-parcel-list", r -> r.path("/customer/parcels").and().method(HttpMethod.GET)
                        .filters(f -> f.filter(jwtAuthFilter)
                                .filter(roleFilter)
                                .rewritePath("/customer/parcels", "/api/v1/parcels")
                                .filter((exchange, chain) -> {

                                    var username = exchange.getRequest().getHeaders()
                                            .getOrEmpty(ParcelCustomHeaders.USERNAME).get(0);

                                    var modifiedUri = UriComponentsBuilder.fromUri(exchange.getRequest().getURI())
                                            .replaceQueryParam("customerUsername", username)
                                            .build(true).toUri();

                                    var mutated = exchange.getRequest().mutate()
                                            .uri(modifiedUri)
                                            .build();

                                    return chain.filter(exchange.mutate()
                                            .request(mutated)
                                            .build());
                                }))
                        .uri("lb://parcel-service"))

                .route("customer-parcel-create", r -> r.path("/customer/parcels").and().method(HttpMethod.POST)
                        .filters(f -> f.filter(jwtAuthFilter)
                                .filter(roleFilter)
                                .rewritePath("/customer/parcels", "/api/v1/parcels"))
                        .uri("lb://parcel-service"))

                .route("customer-parcel-update", r -> r.path("/customer/parcels/{id}").and().method(HttpMethod.PUT)
                        .filters(f -> f.filter(jwtAuthFilter)
                                .filter(roleFilter)
                                .rewritePath("customer/parcels", "/api/v1/parcels"))
                        .uri("lb://parcel-service"))

                .route("customer-parcel-cancel", r -> r.path("/customer/parcels/{id}").and().method(HttpMethod.DELETE)
                        .filters(f -> f.filter(jwtAuthFilter)
                                .filter(roleFilter)
                                .filter((exchange, chain) -> {

                                    var templateVariables = ServerWebExchangeUtils.getUriTemplateVariables(exchange);
                                    var uriTemplate = new UriTemplate("/api/v1/parcels/{id}/status");

                                    var mutated = exchange.getRequest().mutate()
                                            .method(HttpMethod.PUT)
                                            .path(uriTemplate.expand(templateVariables).getPath())
                                            .build();

                                    return chain.filter(exchange.mutate()
                                            .request(mutated)
                                            .build());
                                })
                                .modifyRequestBody(Object.class, ParcelStatus.class, MediaType.APPLICATION_JSON_VALUE,
                                        (exchange, o) -> Mono.just(ParcelStatus.CANCELLED)))
                        .uri("lb://parcel-service"))

                .build();
    }
}
