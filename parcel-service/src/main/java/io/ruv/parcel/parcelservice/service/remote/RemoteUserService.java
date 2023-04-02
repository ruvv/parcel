package io.ruv.parcel.parcelservice.service.remote;

import io.ruv.parcel.user.api.courier.CourierDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "user-service")
public interface RemoteUserService {

    @RequestMapping(value = "/api/v1/couriers/{username}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    CourierDto getCourier(@PathVariable String username);
}
