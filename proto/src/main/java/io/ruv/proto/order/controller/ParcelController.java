package io.ruv.proto.order.controller;

import io.ruv.proto.order.service.ParcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/parcels")
@RequiredArgsConstructor
public class ParcelController {

    private final ParcelService parcelService;

    //todo something
}
