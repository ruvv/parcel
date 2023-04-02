package io.ruv.parcel.parcelservice.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ForbiddenParcelStatusChangeException extends ResponseStatusException {

    public ForbiddenParcelStatusChangeException() {

        super(HttpStatus.FORBIDDEN);
    }
}
