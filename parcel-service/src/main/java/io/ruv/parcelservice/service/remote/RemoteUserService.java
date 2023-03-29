package io.ruv.parcelservice.service.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient(name = "user-service")
public interface RemoteUserService {

    @RequestMapping(value = "/api/v1/couriers/{username}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Object> getCourier(@PathVariable String username);
}
