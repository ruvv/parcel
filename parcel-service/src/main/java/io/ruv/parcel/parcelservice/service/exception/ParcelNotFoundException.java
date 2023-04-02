package io.ruv.parcel.parcelservice.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ParcelNotFoundException extends ResponseStatusException {

    public ParcelNotFoundException(long id) {

        super(HttpStatus.NOT_FOUND, String.format("Parcel '%d' was not found.", id));
    }
}
