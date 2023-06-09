package io.ruv.parcel.gateway.service.filters;

import io.ruv.parcel.user.api.UserRole;
import io.ruv.parcel.user.api.auth.ParcelCustomHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RequireRoleFilter implements GatewayFilter {

    private final UserRole requiredRole;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        var rolesHeader = exchange.getRequest().getHeaders().getOrEmpty(ParcelCustomHeaders.ROLES);
        var roles = UserRole.tryParse(rolesHeader);

        if (!roles.contains(requiredRole)) {

            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }
}
