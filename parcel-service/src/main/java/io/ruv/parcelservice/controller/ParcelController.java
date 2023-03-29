package io.ruv.parcelservice.controller;

import io.ruv.parcelservice.api.*;
import io.ruv.parcelservice.service.ParcelService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/parcels")
@RequiredArgsConstructor
public class ParcelController {

    private final ParcelService parcelService;

    @GetMapping("{id}")
    public ParcelDto parcel(@PathVariable long id,
                            UserInfo userInfo) {

        return parcelService.getParcel(id, userInfo);
    }

    @GetMapping
    public List<ParcelDto> parcels(@RequestParam(required = false, defaultValue = "0") int page,
                                   @RequestParam(required = false, defaultValue = "20") int size,
                                   @RequestParam(required = false) String customerUsername,
                                   @RequestParam(required = false) String courierUsername) {

        return parcelService.findParcels(page, size, customerUsername, courierUsername);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParcelDto create(@RequestBody ParcelCreateDto parcelCreateDto,
                            UserInfo userInfo,
                            HttpServletResponse response) {

        var created = parcelService.createParcel(parcelCreateDto, userInfo);

        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUriString();

        response.setHeader(HttpHeaders.LOCATION, location);

        return created;
    }

    @PutMapping("{id}")
    public ParcelDto update(@PathVariable long id,
                            @RequestBody ParcelUpdateDto parcelUpdateDto,
                            UserInfo userInfo) {

        return parcelService.updateParcel(id, parcelUpdateDto, userInfo);
    }

    @PutMapping("{id}/status")
    public ParcelDto updateStatus(@PathVariable long id,
                                  @RequestBody ParcelStatus status,
                                  UserInfo userInfo) {

        return parcelService.updateParcelStatus(id, status, userInfo);
    }

    @PutMapping("{id}/assignee")
    public ParcelDto assign(@PathVariable long id,
                            @RequestBody String courierUsername,
                            UserInfo userInfo) {

        return parcelService.updateAssignee(id, courierUsername, userInfo);
    }
}
