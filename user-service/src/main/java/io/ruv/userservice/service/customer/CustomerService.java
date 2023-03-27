package io.ruv.userservice.service.customer;


import io.ruv.userservice.api.customer.CustomerDto;
import io.ruv.userservice.api.customer.CustomerRegistrationDto;
import io.ruv.userservice.api.customer.CustomerUpdateDto;

public interface CustomerService {

    CustomerDto getCustomer(String username);

    CustomerDto createCustomer(CustomerRegistrationDto customerRegistrationDto);

    CustomerDto updateCustomer(String username, CustomerUpdateDto customerUpdateDto);

}
