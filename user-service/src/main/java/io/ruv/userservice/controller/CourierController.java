package io.ruv.userservice.controller;

import io.ruv.userservice.api.CourierStatus;
import io.ruv.userservice.api.courier.CourierCreateDto;
import io.ruv.userservice.api.courier.CourierDto;
import io.ruv.userservice.api.courier.CourierUpdateDto;
import io.ruv.userservice.api.courier.Location;
import io.ruv.userservice.service.courier.CourierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/couriers")
@RequiredArgsConstructor
public class CourierController {

    private final CourierService courierService;

    @GetMapping("{username}")
    public CourierDto courier(@PathVariable String username) {

        return courierService.getCourier(username);
    }

    @GetMapping
    public List<CourierDto> couriers(@RequestParam(required = false, defaultValue = "0") int page,
                                     @RequestParam(required = false, defaultValue = "20") int size) {

        return courierService.findCouriers(page, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourierDto createCourier(@RequestBody CourierCreateDto courierCreateDto) {

        return courierService.createCourier(courierCreateDto);
    }

    @PutMapping("{username}")
    public CourierDto updateCourierByUsername(@PathVariable String username,
                                              @RequestBody CourierUpdateDto courierUpdateDto) {

        return courierService.updateCourier(username, courierUpdateDto);
    }

    @PutMapping("{username}/status")
    public CourierDto updateCourierStatus(@PathVariable String username,
                                          @RequestBody CourierStatus courierStatus) {

        return courierService.updateCourierStatus(username, courierStatus);
    }

    @PutMapping("{username}/location")
    public CourierDto updateLocation(@PathVariable String username,
                                     @RequestBody Location location) {

        return courierService.updateCourierLocation(username, location);
    }
}
