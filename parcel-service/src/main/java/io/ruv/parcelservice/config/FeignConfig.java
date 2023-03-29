package io.ruv.parcelservice.config;

import io.ruv.parcelservice.service.remote.RemoteUserService;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = RemoteUserService.class)
public class FeignConfig {
}
