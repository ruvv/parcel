package io.ruv.parcel.parcel.api;

public enum ParcelStatus {

    CREATED,
    ACCEPTED,
    REJECTED,
    ASSIGNED,
    DELIVERING,
    DELIVERY_PROBLEM,
    DELIVERED,
    CANCELLED;

    public static final String BALANCE_PROCESSED = "BALANCE_PROCESSED";
}
