package io.ruv.userservice.service.courier;

import io.ruv.userservice.api.CourierStatus;
import io.ruv.userservice.api.UserRole;
import io.ruv.userservice.api.courier.CourierCreateDto;
import io.ruv.userservice.api.courier.CourierDto;
import io.ruv.userservice.api.courier.CourierUpdateDto;
import io.ruv.userservice.api.courier.Location;
import io.ruv.userservice.repo.UserRepository;
import io.ruv.userservice.service.courier.converter.CourierConverter;
import io.ruv.userservice.service.exception.UserNotFoundException;
import io.ruv.userservice.service.exception.UsernameConflictException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourierServiceImpl implements CourierService {

    private final UserRepository repository;
    private final CourierConverter converter;

    @Override
    public CourierDto getCourier(String username) {

        var entity = repository.findOneByUsernameAndHasRole(username, UserRole.COURIER)
                .orElseThrow(() -> new UserNotFoundException(username));

        return converter.dto(entity);
    }

    @Override
    public List<CourierDto> findCouriers(int page, int size) {

        var pageable = PageRequest.of(page, size);

        return repository.findByHasRole(UserRole.COURIER, pageable).stream()
                .map(converter::dto)
                .collect(Collectors.toList());
    }

    @Override
    public CourierDto createCourier(CourierCreateDto courierCreateDto) {

        repository.findById(courierCreateDto.getUsername())
                .ifPresent(alreadyPresent -> UsernameConflictException.throwFor(alreadyPresent.getUsername()));

        var toSave = converter.entity(courierCreateDto);
        var saved = repository.save(toSave);

        return converter.dto(saved);
    }

    @Override
    public CourierDto updateCourier(String username, CourierUpdateDto courierUpdateDto) {

        var entity = repository.findOneByUsernameAndHasRole(username, UserRole.COURIER)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (courierUpdateDto.getEmail() == null && courierUpdateDto.getPassword() == null) {
            return converter.dto(entity);
        }

        var toSave = converter.update(entity, courierUpdateDto);
        var saved = repository.save(toSave);

        return converter.dto(saved);
    }

    @Override
    @Transactional
    public CourierDto updateCourierStatus(String username, CourierStatus courierStatus) {

        var entity = repository.findOneByUsernameAndHasRole(username, UserRole.COURIER)
                .orElseThrow(() -> new UserNotFoundException(username));

        entity.getCourierExtension().setStatus(courierStatus);
        var saved = repository.save(entity);

        return converter.dto(saved);
    }

    @Override
    @Transactional
    public CourierDto updateCourierLocation(String username, Location location) {

        var entity = repository.findOneByUsernameAndHasRole(username, UserRole.COURIER)
                .orElseThrow(() -> new UserNotFoundException(username));

        entity.getCourierExtension()
                .setLatitude(location.getLatitude())
                .setLongitude(location.getLongitude());
        var saved = repository.save(entity);

        return converter.dto(saved);
    }
}
