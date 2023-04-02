package io.ruv.parcel.userservice.service.courier.converter;

import io.ruv.parcel.user.api.courier.CourierCreateDto;
import io.ruv.parcel.user.api.courier.CourierDto;
import io.ruv.parcel.user.api.courier.CourierUpdateDto;
import io.ruv.parcel.userservice.entity.User;

public interface CourierConverter {

    User entity(CourierCreateDto dto);

    User update(User entity, CourierUpdateDto dto);

    CourierDto dto(User entity);
}
