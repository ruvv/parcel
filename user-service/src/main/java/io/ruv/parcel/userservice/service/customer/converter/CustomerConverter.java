package io.ruv.parcel.userservice.service.customer.converter;

import io.ruv.parcel.user.api.customer.CustomerDto;
import io.ruv.parcel.user.api.customer.CustomerRegistrationDto;
import io.ruv.parcel.user.api.customer.CustomerUpdateDto;
import io.ruv.parcel.userservice.entity.User;

public interface CustomerConverter {

    User entity(CustomerRegistrationDto dto);

    User update(User entity, CustomerUpdateDto dto);

    CustomerDto dto(User entity);
}
