package io.ruv.parcel.gateway.service.filters;

import io.ruv.parcel.gateway.service.JwtParser;
import io.ruv.parcel.user.api.UserRole;
import io.ruv.parcel.user.api.auth.ParcelCustomHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class JwtAuthFilter implements GatewayFilter {

    private final JwtParser jwtParser;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        var request = exchange.getRequest();

        var authHeader = request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION);

        if (!authHeader.isEmpty()) {

            var token = authHeader.get(0).substring(7);
            var parsedToken = jwtParser.parse(token);

            if (parsedToken.valid() && !parsedToken.expired()) {

                var roleStrings = parsedToken.roles().stream()
                        .map(UserRole::toString)
                        .toArray(String[]::new);

                request.mutate()
                        .header(ParcelCustomHeaders.USERNAME, parsedToken.username())
                        .header(ParcelCustomHeaders.ROLES, roleStrings)
                        .build();
            } else {

                return reject(exchange);
            }
        } else {

            return reject(exchange);
        }

        return chain.filter(exchange);
    }

    private Mono<Void> reject(ServerWebExchange exchange) {

        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
