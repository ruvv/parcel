package io.ruv.userservice.service.courier;

import io.ruv.userservice.api.CourierStatus;
import io.ruv.userservice.api.UserRole;
import io.ruv.userservice.api.courier.CourierCreateDto;
import io.ruv.userservice.api.courier.CourierDto;
import io.ruv.userservice.api.courier.CourierUpdateDto;
import io.ruv.userservice.api.courier.Location;
import io.ruv.userservice.entity.CourierExtension;
import io.ruv.userservice.entity.User;
import io.ruv.userservice.repo.UserRepository;
import io.ruv.userservice.service.courier.converter.CourierConverter;
import io.ruv.userservice.service.courier.converter.CourierConverterImpl;
import io.ruv.userservice.service.exception.UserNotFoundException;
import io.ruv.userservice.service.exception.UsernameConflictException;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class CourierServiceImplTest {

    @SuppressWarnings("deprecation")
    private final CourierConverter converter = new CourierConverterImpl(NoOpPasswordEncoder.getInstance());
    private final UserRepository repository = mock(UserRepository.class);
    private final CourierService service = new CourierServiceImpl(repository, converter);

    private final User entity = new User()
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

    private final User updatedEntity = new User()
            .setUsername("alice")
            .setEmail("alice-new-email@yahoo.com")
            .setPassword("alice-new-pw")
            .setBalance(0)
            .setCourierExtension(new CourierExtension()
                    .setStatus(CourierStatus.ON_DUTY)
                    .setLatitude("new-lat")
                    .setLongitude("new-lon"))
            .setRoles(List.of(UserRole.COURIER))
            .setEnabled(true);
    private final CourierCreateDto createDto = new CourierCreateDto()
            .setUsername("alice")
            .setEmail("alice@yahoo.com")
            .setPassword("alice-pw");

    private final CourierUpdateDto updateDto = new CourierUpdateDto()
            .setEmail("alice-new-email@yahoo.com")
            .setPassword("alice-new-pw");

    private final CourierDto dto = new CourierDto()
            .setUsername("alice")
            .setEmail("alice@yahoo.com")
            .setCourierStatus(CourierStatus.OFF_DUTY)
            .setLatitude("lat")
            .setLongitude("lon");

    private final CourierDto updatedDto = new CourierDto()
            .setUsername("alice")
            .setEmail("alice-new-email@yahoo.com")
            .setCourierStatus(CourierStatus.ON_DUTY)
            .setLatitude("new-lat")
            .setLongitude("new-lon");

    private final Location locationDto = new Location("new-lat", "new-lon");

    @Test
    public void getCourierExistingReturnsCourier() {

        when(repository.findOneByUsernameAndHasRole(dto.getUsername(), UserRole.COURIER))
                .thenReturn(Optional.of(entity));

        var result = service.getCourier(dto.getUsername());

        assertThat(result).isEqualTo(dto);
    }

    @Test
    public void getCourierNonExistingThrowsUserNotFound() {

        when(repository.findOneByUsernameAndHasRole(dto.getUsername(), UserRole.COURIER))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getCourier(dto.getUsername()))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    public void findCouriersExistingReturnsCourierList() {

        when(repository.findByHasRole(eq(UserRole.COURIER), any()))
                .thenReturn(List.of(entity));

        var result = service.findCouriers(0, 20);

        assertThat(result)
                .isInstanceOf(List.class)
                .hasSize(1)
                .hasSameElementsAs(List.of(dto));
    }

    @Test
    public void findCouriersNonExistingReturnsEmptyList() {

        when(repository.findByHasRole(eq(UserRole.COURIER), any()))
                .thenReturn(List.of());

        var result = service.findCouriers(0, 20);

        assertThat(result)
                .isInstanceOf(List.class)
                .isEmpty();
    }

    @Test
    public void createCourierNonDuplicateUsernameReturnsCourier() {

        when(repository.findById(createDto.getUsername()))
                .thenReturn(Optional.empty());

        when(repository.save(any()))
                .thenReturn(entity);

        var result = service.createCourier(createDto);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    public void createCourierDuplicateUsernameThrowsUsernameConflict() {

        when(repository.findById(createDto.getUsername()))
                .thenReturn(Optional.of(entity));

        assertThatThrownBy(() -> service.createCourier(createDto))
                .isInstanceOf(UsernameConflictException.class);
    }

    @Test
    public void updateCourierExistingReturnsCourier() {

        when(repository.findOneByUsernameAndHasRole(dto.getUsername(), UserRole.COURIER))
                .thenReturn(Optional.of(entity));

        when(repository.save(any()))
                .thenReturn(updatedEntity);

        var result = service.updateCourier(dto.getUsername(), updateDto);

        assertThat(result).isEqualTo(updatedDto);
    }

    @Test
    public void updateCourierNonExistingThrowsUserNotFound() {

        when(repository.findOneByUsernameAndHasRole(dto.getUsername(), UserRole.COURIER))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateCourier(dto.getUsername(), updateDto))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    public void updateCourierStatusExistingReturnsCourier() {

        when(repository.findOneByUsernameAndHasRole(dto.getUsername(), UserRole.COURIER))
                .thenReturn(Optional.of(entity));

        when(repository.save(any()))
                .thenReturn(updatedEntity);

        var result = service.updateCourierStatus(dto.getUsername(), CourierStatus.ON_DUTY);

        assertThat(result).isEqualTo(updatedDto);
    }

    @Test
    public void updateCourierStatusNonExistingThrowsUserNotFound() {

        when(repository.findOneByUsernameAndHasRole(dto.getUsername(), UserRole.COURIER))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateCourierStatus(dto.getUsername(), CourierStatus.ON_DUTY))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    public void updateCourierLocationExistingReturnsCourier() {

        when(repository.findOneByUsernameAndHasRole(dto.getUsername(), UserRole.COURIER))
                .thenReturn(Optional.of(entity));

        when(repository.save(any()))
                .thenReturn(updatedEntity);

        var result = service.updateCourierLocation(dto.getUsername(), locationDto);

        assertThat(result).isEqualTo(updatedDto);
    }

    @Test
    public void updateCourierLocationNonExistingThrowsUserNotFound() {

        when(repository.findOneByUsernameAndHasRole(dto.getUsername(), UserRole.COURIER))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateCourierLocation(dto.getUsername(), locationDto))
                .isInstanceOf(UserNotFoundException.class);
    }
}
