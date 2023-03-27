package io.ruv.userservice.service.courier.converter;

import io.ruv.userservice.api.courier.CourierCreateDto;
import io.ruv.userservice.api.courier.CourierDto;
import io.ruv.userservice.api.courier.CourierUpdateDto;
import io.ruv.userservice.entity.User;

public interface CourierConverter {

    User entity(CourierCreateDto dto);

    User update(User entity, CourierUpdateDto dto);

    CourierDto dto(User entity);
}
