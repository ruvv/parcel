package io.ruv.proto.user.service.converter;

import io.ruv.proto.user.api.courier.CourierCreateDto;
import io.ruv.proto.user.api.courier.CourierDto;
import io.ruv.proto.user.api.courier.CourierStatus;
import io.ruv.proto.user.api.courier.CourierUpdateDto;
import io.ruv.proto.user.api.customer.CustomerDto;
import io.ruv.proto.user.api.customer.CustomerRegistrationDto;
import io.ruv.proto.user.api.customer.CustomerUpdateDto;
import io.ruv.proto.user.entity.CourierExtension;
import io.ruv.proto.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserConverter {

    private final PasswordEncoder passwordEncoder;

    public User toEntity(CustomerRegistrationDto dto) {

        return new User(
                dto.getUsername(),
                dto.getEmail(),
                passwordEncoder.encode(dto.getPassword()),
                0,
                null,
                List.of("customer"),
                true);
    }

    public User updateEntity(User entity, CustomerUpdateDto dto) {

        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }

        if (dto.getPassword() != null) {
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return entity;
    }

    public CustomerDto toCustomerDto(User entity) {

        return new CustomerDto(
                entity.getUsername(),
                entity.getEmail(),
                entity.getBalance());
    }

    public User toEntity(CourierCreateDto dto) {

        return new User(dto.getUsername(),
                dto.getEmail(),
                passwordEncoder.encode(dto.getPassword()),
                0,
                new CourierExtension(null, null, CourierStatus.OFF_DUTY, null, null),
                List.of("courier"),
                true);
    }

    public User updateEntity(User entity, CourierUpdateDto dto) {

        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null) {
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return entity;
    }

    public CourierDto toCourierDto(User entity) {

        return new CourierDto(
                entity.getUsername(),
                entity.getEmail(),
                entity.getCourierExtension().getStatus(),
                entity.getCourierExtension().getLatitude(),
                entity.getCourierExtension().getLongitude());
    }
}
