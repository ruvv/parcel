package io.ruv.proto.user.controller;

import io.ruv.proto.order.api.ParcelDto;
import io.ruv.proto.order.api.ParcelStatus;
import io.ruv.proto.order.service.ParcelService;
import io.ruv.proto.user.api.courier.CourierCreateDto;
import io.ruv.proto.user.api.courier.CourierDto;
import io.ruv.proto.user.api.courier.CourierUpdateDto;
import io.ruv.proto.user.service.CourierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AdministrationController {

    private final ParcelService parcelService;
    private final CourierService courierService;

    @GetMapping("parcels")
    public List<ParcelDto> parcels(@RequestParam(required = false, defaultValue = "0") int page,
                                   @RequestParam(required = false, defaultValue = "20") int size,
                                   @RequestParam(required = false) ParcelStatus parcelStatus,
                                   @RequestParam(required = false) String customerUsername,
                                   @RequestParam(required = false) String courierUsername) {

        return parcelService.find(page, size, parcelStatus, customerUsername, courierUsername);
    }

    @GetMapping("parcels/{id}")
    public ParcelDto parcelById(@PathVariable long id) {

        return parcelService.findById(id);
    }

    @PutMapping(value = "parcels/{id}/assignee")
    public ParcelDto assignParcel(@PathVariable long id, @RequestBody String courierUsername) {

        return parcelService.assignToCourier(id, courierUsername);
    }

    @DeleteMapping(value = "parcels/{id}/assignee")
    public ParcelDto unAssignParcel(@PathVariable long id) {

        return parcelService.unAssign(id);
    }

    @PutMapping(value = "parcels/{id}/status")
    public ParcelDto updateParcelStatus(@PathVariable long id, @RequestBody ParcelStatus status) {

        return parcelService.updateParcelStatus(id, status);
    }

    @GetMapping("couriers")
    public List<CourierDto> couriers(@RequestParam(required = false, defaultValue = "0") int page,
                                     @RequestParam(required = false, defaultValue = "20") int size) {

        return courierService.findCouriers(page, size);
    }

    @PostMapping("couriers")
    public CourierDto createCourier(@RequestBody CourierCreateDto courierCreateDto) {

        return courierService.createCourier(courierCreateDto);
    }

    @PutMapping("couriers/{username}")
    public CourierDto updateCourierByUsername(@PathVariable String username,
                                              @RequestBody CourierUpdateDto courierUpdateDto) {

        return courierService.updateCourierByUsername(username, courierUpdateDto);
    }
}
