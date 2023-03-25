package io.ruv.proto.user.controller;

import io.ruv.proto.order.api.ParcelDto;
import io.ruv.proto.order.api.ParcelStatus;
import io.ruv.proto.order.service.ParcelService;
import io.ruv.proto.user.api.courier.CourierDto;
import io.ruv.proto.user.api.courier.CourierStatus;
import io.ruv.proto.user.api.courier.Location;
import io.ruv.proto.user.service.CourierService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courier")
@RequiredArgsConstructor
public class CourierController {

    private final CourierService courierService;
    private final ParcelService parcelService;

    @GetMapping
    public CourierDto me() {

        return courierService.currentCourier();
    }

    @GetMapping("parcels")
    public List<ParcelDto> myParcels(@RequestParam(required = false, defaultValue = "0") int page,
                                     @RequestParam(required = false, defaultValue = "20") int size,
                                     @RequestParam(required = false) ParcelStatus parcelStatus) {

        var userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return parcelService.find(page, size, parcelStatus, null, userDetails.getUsername());
    }

    @PutMapping(value = "parcels/{id}/status")
    public ParcelDto updateParcelStatus(@PathVariable long id,
                                        @RequestBody ParcelStatus status) {

        return parcelService.updateParcelStatus(id, status);
    }

    @PutMapping(value = "status")
    public CourierDto updateCourierStatus(@RequestBody CourierStatus courierStatus) {

        return courierService.updateCurrentCourierStatus(courierStatus);
    }

    @PutMapping("location")
    public CourierDto updateLocation(@RequestBody Location location) {

        return courierService.updateCurrentCourierLocation(location);
    }
}
