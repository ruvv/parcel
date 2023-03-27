package io.ruv.userservice.controller;

import io.ruv.userservice.api.customer.CustomerDto;
import io.ruv.userservice.api.customer.CustomerRegistrationDto;
import io.ruv.userservice.api.customer.CustomerUpdateDto;
import io.ruv.userservice.service.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("{username}")
    public CustomerDto customer(@PathVariable String username) {

        return customerService.getCustomer(username);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto register(@RequestBody CustomerRegistrationDto customer) {

        return customerService.createCustomer(customer);
    }

    @PutMapping("{username}")
    public CustomerDto update(@PathVariable String username,
                              @RequestBody CustomerUpdateDto update) {

        return customerService.updateCustomer(username, update);
    }
}
