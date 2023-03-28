package io.ruv.userservice.controller;

import io.ruv.userservice.api.CourierStatus;
import io.ruv.userservice.api.courier.CourierCreateDto;
import io.ruv.userservice.api.courier.CourierDto;
import io.ruv.userservice.api.courier.CourierUpdateDto;
import io.ruv.userservice.api.courier.Location;
import io.ruv.userservice.service.courier.CourierService;
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

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1/couriers")
@RequiredArgsConstructor
public class CourierController {

    private final CourierService courierService;

    @Operation(summary = "Get a courier by his username")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found the courier"),
            @ApiResponse(responseCode = "404", description = "Courier with provided username was not found", content = @Content)
    })
    @GetMapping(value = "{username}", produces = APPLICATION_JSON_VALUE)
    public CourierDto courier(
            @Parameter(description = "Username of a courier") @PathVariable String username) {

        return courierService.getCourier(username);
    }

    @Operation(summary = "Get paginated list of all couriers")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Search completed")
    })
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public List<CourierDto> couriers(
            @Parameter(description = "Page number") @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(required = false, defaultValue = "20") int size) {

        return courierService.findCouriers(page, size);
    }

    @Operation(summary = "Create a new courier")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Courier crated"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content),
            @ApiResponse(responseCode = "409", description = "Provided username is already in use", content = @Content)
    })
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CourierDto createCourier(
            @Parameter(description = "Courier info") @Valid @RequestBody CourierCreateDto courierCreateDto,
            HttpServletResponse response) {

        var created = courierService.createCourier(courierCreateDto);

        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{username}")
                .buildAndExpand(created.getUsername())
                .toUriString();

        response.setHeader(HttpHeaders.LOCATION, location);

        return created;
    }

    @Operation(summary = "Update existing courier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Courier updated"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content),
            @ApiResponse(responseCode = "404", description = "Courier with provided username was not found", content = @Content)
    })
    @PutMapping(value = "{username}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public CourierDto updateCourierByUsername(
            @Parameter(description = "Username of a courier") @PathVariable String username,
            @Parameter(description = "Courier info") @Valid @RequestBody CourierUpdateDto courierUpdateDto) {

        return courierService.updateCourier(username, courierUpdateDto);
    }

    @Operation(summary = "Update status of an existing courier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Courier status updated"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content),
            @ApiResponse(responseCode = "404", description = "Courier with provided username was not found", content = @Content)
    })
    @PutMapping(value = "{username}/status", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public CourierDto updateCourierStatus(
            @Parameter(description = "Username of a courier") @PathVariable String username,
            @Parameter(description = "Courier status") @RequestBody CourierStatus courierStatus) {

        return courierService.updateCourierStatus(username, courierStatus);
    }

    @Operation(summary = "Update location of an existing courier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Courier location updated"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content),
            @ApiResponse(responseCode = "404", description = "Courier with provided username was not found", content = @Content)
    })
    @PutMapping(value = "{username}/location", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public CourierDto updateLocation(
            @Parameter(description = "Username of a courier") @PathVariable String username,
            @Parameter(description = "Courier location") @Valid @RequestBody Location location) {

        return courierService.updateCourierLocation(username, location);
    }
}
