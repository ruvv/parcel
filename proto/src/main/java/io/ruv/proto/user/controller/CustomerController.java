package io.ruv.proto.user.controller;

import io.ruv.proto.order.api.ParcelCreateDto;
import io.ruv.proto.order.api.ParcelDto;
import io.ruv.proto.order.api.ParcelStatus;
import io.ruv.proto.order.api.ParcelUpdateDto;
import io.ruv.proto.order.service.ParcelService;
import io.ruv.proto.user.api.customer.CustomerDto;
import io.ruv.proto.user.api.customer.CustomerRegistrationDto;
import io.ruv.proto.user.api.customer.CustomerUpdateDto;
import io.ruv.proto.user.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final ParcelService parcelService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto register(@RequestBody CustomerRegistrationDto customer) {

        return customerService.createCustomer(customer);
    }

    @GetMapping
    public CustomerDto me() {

        return customerService.currentCustomer();
    }

    @PutMapping
    public CustomerDto update(@RequestBody CustomerUpdateDto update) {

        return customerService.updateCurrentCustomer(update);
    }

    @PostMapping("parcels")
    public ParcelDto createParcel(@RequestBody ParcelCreateDto parcel) {

        var userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return parcelService.createParcel(userDetails.getUsername(), parcel);
    }

    @GetMapping("parcels")
    public List<ParcelDto> myParcels(@RequestParam(required = false, defaultValue = "0") int page,
                                     @RequestParam(required = false, defaultValue = "20") int size,
                                     @RequestParam(required = false) ParcelStatus parcelStatus) {

        var userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return parcelService.find(page, size, parcelStatus, userDetails.getUsername(), null);
    }

    @PutMapping("parcels/{id}")
    public ParcelDto updateParcel(@PathVariable long id,
                                  @RequestBody ParcelUpdateDto parcelUpdate) {

        return parcelService.updateParcel(id, parcelUpdate);
    }

    @DeleteMapping("parcels/{id}")
    public ParcelDto cancelParcel(@PathVariable long id) {

        return parcelService.updateParcelStatus(id, ParcelStatus.CANCELLED);
    }
}
