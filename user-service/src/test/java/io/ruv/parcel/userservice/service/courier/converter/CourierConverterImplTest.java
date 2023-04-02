package io.ruv.parcel.userservice.service.courier.converter;

import io.ruv.parcel.user.api.CourierStatus;
import io.ruv.parcel.user.api.UserRole;
import io.ruv.parcel.user.api.courier.CourierCreateDto;
import io.ruv.parcel.user.api.courier.CourierDto;
import io.ruv.parcel.user.api.courier.CourierUpdateDto;
import io.ruv.parcel.userservice.entity.CourierExtension;
import io.ruv.parcel.userservice.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import java.util.List;

public class CourierConverterImplTest {

    @SuppressWarnings("deprecation")
    private final CourierConverter converter = new CourierConverterImpl(NoOpPasswordEncoder.getInstance());

    @Test
    void createDtoConvertsToEntityCorrectly() {

        var createDto = new CourierCreateDto()
                .setUsername("alice")
                .setEmail("alice@yahoo.com")
                .setPassword("alice-pw");

        var expectedEntity = new User()
                .setUsername("alice")
                .setEmail("alice@yahoo.com")
                .setPassword("alice-pw")
                .setBalance(0)
                .setCourierExtension(new CourierExtension()
                        .setStatus(CourierStatus.OFF_DUTY))
                .setRoles(List.of(UserRole.COURIER))
                .setEnabled(true);

        var result = converter.entity(createDto);

        Assertions.assertThat(result).isEqualTo(expectedEntity);
    }

    @Test
    void updateDtoUpdatesEntityCorrectly() {

        var savedEntity = new User()
                .setUsername("alice")
                .setEmail("alice@yahoo.com")
                .setPassword("alice-pw")
                .setBalance(0)
                .setCourierExtension(new CourierExtension()
                        .setStatus(CourierStatus.OFF_DUTY)
                        .setLatitude("lat")
                        .setLongitude("lon"))
                .setRoles(List.of(UserRole.COURIER))
                .setEnabled(true);

        var updateDto = new CourierUpdateDto()
                .setEmail("alice-new-email@yahoo.com")
                .setPassword("alice-new-pw");

        var expectedEntity = new User()
                .setUsername("alice")
                .setEmail("alice-new-email@yahoo.com")
                .setPassword("alice-new-pw")
                .setBalance(0)
                .setCourierExtension(new CourierExtension()
                        .setStatus(CourierStatus.OFF_DUTY))
                .setRoles(List.of(UserRole.COURIER))
                .setEnabled(true);

        var result = converter.update(savedEntity, updateDto);

        Assertions.assertThat(result).isEqualTo(expectedEntity);
    }

    @Test
    void entityConvertsToDtoCorrectly() {

        var savedEntity = new User()
                .setUsername("alice")
                .setEmail("alice@yahoo.com")
                .setPassword("alice-pw")
                .setBalance(0)
                .setCourierExtension(new CourierExtension()
                        .setStatus(CourierStatus.OFF_DUTY)
                        .setLatitude("lat")
                        .setLongitude("lon"))
                .setRoles(List.of(UserRole.COURIER))
                .setEnabled(true);

        var expectedDto = new CourierDto()
                .setUsername("alice")
                .setEmail("alice@yahoo.com")
                .setCourierStatus(CourierStatus.OFF_DUTY)
                .setLatitude("lat")
                .setLongitude("lon");

        var result = converter.dto(savedEntity);

        Assertions.assertThat(result).isEqualTo(expectedDto);
    }
}
