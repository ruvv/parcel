package io.ruv.proto.user.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum UserRole {
    CUSTOMER("customer"),
    ADMIN("admin"),
    COURIER("courier");

    public final String value;
}
