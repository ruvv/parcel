package io.ruv.parcel.user.api.auth;

import io.ruv.parcel.user.api.UserRole;

import java.util.Set;

public record UserInfo(String username, Set<UserRole> roles) {
}
