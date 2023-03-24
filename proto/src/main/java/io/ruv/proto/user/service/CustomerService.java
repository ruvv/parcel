package io.ruv.proto.user.service;

import io.ruv.proto.user.api.customer.CustomerDto;
import io.ruv.proto.user.api.customer.CustomerRegistrationDto;
import io.ruv.proto.user.api.customer.CustomerUpdateDto;

public interface CustomerService {

    CustomerDto currentCustomer();

    CustomerDto createCustomer(CustomerRegistrationDto customerRegistrationDto);

    CustomerDto updateCurrentCustomer(CustomerUpdateDto customerUpdateDto);

}
