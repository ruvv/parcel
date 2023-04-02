package io.ruv.parcel.parcelservice;

import io.ruv.parcel.parcelservice.repo.ParcelRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing
@EnableJpaRepositories(basePackageClasses = ParcelRepository.class)
@OpenAPIDefinition(info =
@Info(title = "Parcels API", version = "v1", description = "Parcels service API v1.0")
)
public class ParcelServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParcelServiceApplication.class, args);
    }

}
