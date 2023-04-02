package io.ruv.parcel.parcelservice.config;

import io.ruv.parcel.parcelservice.service.remote.RemoteUserService;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = RemoteUserService.class)
public class FeignConfig {
}
