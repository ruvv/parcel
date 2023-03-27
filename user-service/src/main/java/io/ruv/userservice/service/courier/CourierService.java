package io.ruv.userservice.service.courier;

import io.ruv.userservice.api.CourierStatus;
import io.ruv.userservice.api.courier.CourierCreateDto;
import io.ruv.userservice.api.courier.CourierDto;
import io.ruv.userservice.api.courier.CourierUpdateDto;
import io.ruv.userservice.api.courier.Location;

import java.util.List;

public interface CourierService {

    CourierDto getCourier(String username);

    List<CourierDto> findCouriers(int page, int size);

    CourierDto createCourier(CourierCreateDto courierCreateDto);

    CourierDto updateCourier(String username, CourierUpdateDto courierUpdateDto);

    CourierDto updateCourierStatus(String username, CourierStatus courierStatus);

    CourierDto updateCourierLocation(String username, Location location);
//
//    CourierDto createCourier(CourierCreateDto courierCreateDto);
//
//    CourierDto updateCourierByUsername(String username, CourierUpdateDto courierUpdateDto);
//
//    CourierDto findCourierById(String username);
//

}
