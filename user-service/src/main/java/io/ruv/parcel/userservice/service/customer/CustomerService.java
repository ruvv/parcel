package io.ruv.parcel.userservice.service.customer;


import io.ruv.parcel.user.api.customer.BalanceChangeDto;
import io.ruv.parcel.user.api.customer.CustomerDto;
import io.ruv.parcel.user.api.customer.CustomerRegistrationDto;
import io.ruv.parcel.user.api.customer.CustomerUpdateDto;

public interface CustomerService {

    CustomerDto getCustomer(String username);

    CustomerDto createCustomer(CustomerRegistrationDto customerRegistrationDto);

    CustomerDto updateCustomer(String username, CustomerUpdateDto customerUpdateDto);

    CustomerDto updateBalance(String username, BalanceChangeDto balanceChangeDto);

}
