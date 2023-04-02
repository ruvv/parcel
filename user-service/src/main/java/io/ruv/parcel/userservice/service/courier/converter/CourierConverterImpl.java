package io.ruv.parcel.userservice.service.courier.converter;

import io.ruv.parcel.user.api.CourierStatus;
import io.ruv.parcel.user.api.UserRole;
import io.ruv.parcel.user.api.courier.CourierCreateDto;
import io.ruv.parcel.user.api.courier.CourierDto;
import io.ruv.parcel.user.api.courier.CourierUpdateDto;
import io.ruv.parcel.userservice.entity.CourierExtension;
import io.ruv.parcel.userservice.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourierConverterImpl implements CourierConverter {

    private final PasswordEncoder passwordEncoder;

    @Override
    public User entity(CourierCreateDto dto) {

        return new User(dto.getUsername(),
                dto.getEmail(),
                passwordEncoder.encode(dto.getPassword()),
                0,
                0,
                new CourierExtension(null, null, CourierStatus.OFF_DUTY, null, null),
                List.of(UserRole.COURIER),
                true,
                0);
    }

    @Override
    public User update(User entity, CourierUpdateDto dto) {

        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null) {
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return entity;
    }

    @Override
    public CourierDto dto(User entity) {

        return new CourierDto(
                entity.getUsername(),
                entity.getEmail(),
                entity.getCourierExtension().getStatus(),
                entity.getCourierExtension().getLatitude(),
                entity.getCourierExtension().getLongitude());
    }
}
