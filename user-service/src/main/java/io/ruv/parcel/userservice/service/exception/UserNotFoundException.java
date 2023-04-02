package io.ruv.parcel.userservice.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserNotFoundException extends ResponseStatusException {

    public UserNotFoundException(String username) {
        super(HttpStatus.NOT_FOUND, String.format("User '%s' was not found.", username));
    }
}
