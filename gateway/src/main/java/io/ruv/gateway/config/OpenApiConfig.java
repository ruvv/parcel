package io.ruv.gateway.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    @Lazy(false)
    public List<GroupedOpenApi> apis() {

        return List.of(GroupedOpenApi.builder()
                .pathsToMatch("/user-service/**")
                .group("user-service")
                .build());
    }
}
