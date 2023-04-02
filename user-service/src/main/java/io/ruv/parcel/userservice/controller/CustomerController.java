package io.ruv.parcel.userservice.controller;

import io.ruv.parcel.user.api.customer.BalanceChangeDto;
import io.ruv.parcel.user.api.customer.CustomerDto;
import io.ruv.parcel.user.api.customer.CustomerRegistrationDto;
import io.ruv.parcel.user.api.customer.CustomerUpdateDto;
import io.ruv.parcel.userservice.service.customer.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @Operation(summary = "Get customer by his username")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found the customer"),
            @ApiResponse(responseCode = "404", description = "Customer with provided username was not found", content = @Content)
    })
    @GetMapping(value = "{username}", produces = APPLICATION_JSON_VALUE)
    public CustomerDto customer(
            @Parameter(description = "Username of a customer") @PathVariable String username) {

        return customerService.getCustomer(username);
    }

    @Operation(summary = "Create a new customer")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Customer created"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content),
            @ApiResponse(responseCode = "409", description = "Provided username is already in use", content = @Content)
    })
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto register(
            @Parameter(description = "Customer info") @Valid @RequestBody CustomerRegistrationDto customer,
            HttpServletResponse response) {

        var created = customerService.createCustomer(customer);

        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{username}")
                .buildAndExpand(created.getUsername())
                .toUriString();

        response.setHeader(HttpHeaders.LOCATION, location);

        return created;
    }

    @Operation(summary = "Update existing customer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer updated"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content),
            @ApiResponse(responseCode = "404", description = "Customer with provided username was not found", content = @Content)
    })
    @PutMapping("{username}")
    public CustomerDto update(
            @Parameter(description = "Username of a customer") @PathVariable String username,
            @Parameter(description = "Customer info") @Valid @RequestBody CustomerUpdateDto update) {

        return customerService.updateCustomer(username, update);
    }

    @Operation(summary = "Manipulate existing customer balance and locked balance")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Balance changed"),
            @ApiResponse(responseCode = "404", description = "Customer with provided username was not found"),
            @ApiResponse(responseCode = "409", description = "Insufficient balance")
    })
    @PatchMapping("{username}/balance")
    public CustomerDto changeBalance(
            @Parameter(description = "Username of a customer") @PathVariable String username,
            @Parameter(description = "Balance changes") @RequestBody BalanceChangeDto balanceChangeDto) {

        return customerService.updateBalance(username, balanceChangeDto);
    }
}
