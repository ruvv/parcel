package io.ruv.parcel.userservice.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BadCredentialsException extends ResponseStatusException {

    public BadCredentialsException(String username) {
        super(HttpStatus.UNAUTHORIZED, String.format("Incorrect credentials for user '%s'.", username));
    }
}
