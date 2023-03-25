package io.ruv.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouteLocator dummy(RouteLocatorBuilder rlb) {

        return rlb.routes()
                .route("sample", r -> r.path("/discovery-client")
                        .filters(f -> f.stripPrefix(1)
                                .addRequestHeader(HttpHeaders.AUTHORIZATION, "Basic Ym9iOjEyMw=="))
                        .uri("lb://sample-client"))
                .build();
    }

}
