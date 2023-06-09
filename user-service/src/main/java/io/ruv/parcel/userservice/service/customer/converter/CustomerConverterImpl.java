package io.ruv.parcel.userservice.service.customer.converter;

import io.ruv.parcel.user.api.UserRole;
import io.ruv.parcel.user.api.customer.CustomerDto;
import io.ruv.parcel.user.api.customer.CustomerRegistrationDto;
import io.ruv.parcel.user.api.customer.CustomerUpdateDto;
import io.ruv.parcel.userservice.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerConverterImpl implements CustomerConverter {

    private final PasswordEncoder passwordEncoder;

    @Override
    public User entity(CustomerRegistrationDto dto) {

        return new User(
                dto.getUsername(),
                dto.getEmail(),
                passwordEncoder.encode(dto.getPassword()),
                5,
                0,
                null,
                List.of(UserRole.CUSTOMER),
                true,
                0);
    }

    @Override
    public User update(User entity, CustomerUpdateDto dto) {

        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }

        if (dto.getPassword() != null) {
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return entity;
    }

    @Override
    public CustomerDto dto(User entity) {

        return new CustomerDto(
                entity.getUsername(),
                entity.getEmail(),
                entity.getBalance() - entity.getLockedBalance());
    }
}
