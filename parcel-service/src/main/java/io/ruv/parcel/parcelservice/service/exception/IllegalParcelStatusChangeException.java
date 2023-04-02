package io.ruv.parcel.parcelservice.service.exception;

import io.ruv.parcel.parcel.api.ParcelStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class IllegalParcelStatusChangeException extends ResponseStatusException {

    public IllegalParcelStatusChangeException(long parcelId, ParcelStatus currentStatus, ParcelStatus newStatus) {

        super(HttpStatus.CONFLICT,
                String.format("Failed to change status for parcel '%d': can not transition from '%s' to '%s'.",
                        parcelId, currentStatus, newStatus));
    }
}
