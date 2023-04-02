package io.ruv.parcel.userservice.service.courier;

import io.ruv.parcel.user.api.CourierStatus;
import io.ruv.parcel.user.api.courier.CourierCreateDto;
import io.ruv.parcel.user.api.courier.CourierDto;
import io.ruv.parcel.user.api.courier.CourierUpdateDto;
import io.ruv.parcel.user.api.courier.Location;

import java.util.List;

public interface CourierService {

    CourierDto getCourier(String username);

    List<CourierDto> findCouriers(int page, int size);

    CourierDto createCourier(CourierCreateDto courierCreateDto);

    CourierDto updateCourier(String username, CourierUpdateDto courierUpdateDto);

    CourierDto updateCourierStatus(String username, CourierStatus courierStatus);

    CourierDto updateCourierLocation(String username, Location location);

}
