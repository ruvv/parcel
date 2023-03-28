package io.ruv.gateway.routes;

import com.netflix.appinfo.ApplicationInfoManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

@Configuration
public class PublicRoutesConfig {

    @Value("${server.port}")
    private int port;

    @Bean
    public RouteLocator publicRoutes(RouteLocatorBuilder rlb) {

        return rlb.routes()

                .route("login", r -> r.path("/signin").and().method(HttpMethod.POST)
                        .filters(f -> f.removeRequestHeader(HttpHeaders.AUTHORIZATION)
                                .setPath("/api/v1/login"))
                        .uri("lb://user-service"))

                .route("customer-registration", r -> r.path("/signup").and().method(HttpMethod.POST)
                        .filters(f -> f.removeRequestHeader(HttpHeaders.AUTHORIZATION)
                                .setPath("/api/v1/customers"))
                        .uri("lb://user-service"))

                .build();
    }

    @Bean
    @Profile("!prod")
    public RouteLocator openApiRoutes(RouteLocatorBuilder rlb, ApplicationInfoManager applicationInfoManager) {

        return rlb.routes()

                .route("openapi", r -> r.path("/v3/api-docs/**")
                        .filters(f -> f.rewritePath("/v3/api-docs/(?<path>.*)", "/$\\{path}/v3/api-docs"))
                        .uri("http://" + applicationInfoManager.getInfo().getHostName() + ":" + port))

                .route("openapi-user-service", r -> r.path("/user-service/v3/api-docs")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://user-service"))

                .build();
    }
}
