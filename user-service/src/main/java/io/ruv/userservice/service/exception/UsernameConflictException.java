package io.ruv.userservice.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UsernameConflictException extends ResponseStatusException {

    public UsernameConflictException(String username) {
        super(HttpStatus.CONFLICT, String.format("Username '%s' is not available.", username));
    }

    public static void throwFor(String username) {
        throw new UsernameConflictException(username);
    }
}
