package io.ruv.userservice.service.customer.converter;

import io.ruv.userservice.api.customer.CustomerDto;
import io.ruv.userservice.api.customer.CustomerRegistrationDto;
import io.ruv.userservice.api.customer.CustomerUpdateDto;
import io.ruv.userservice.entity.User;

public interface CustomerConverter {

    User entity(CustomerRegistrationDto dto);

    User update(User entity, CustomerUpdateDto dto);

    CustomerDto dto(User entity);
}
