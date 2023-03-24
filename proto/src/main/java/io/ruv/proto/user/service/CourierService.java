package io.ruv.proto.user.service;

import io.ruv.proto.user.api.courier.*;

import java.util.List;

public interface CourierService {

    CourierDto currentCourier();

    CourierDto updateCurrentCourierStatus(CourierStatus courierStatus);

    CourierDto updateCurrentCourierLocation(Location location);

    CourierDto createCourier(CourierCreateDto courierCreateDto);

    CourierDto updateCourierByUsername(String username, CourierUpdateDto courierUpdateDto);

    CourierDto findCourierById(String username);

    List<CourierDto> findCouriers(int page, int size);
}
