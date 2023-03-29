package io.ruv.parcelservice.api;

import io.ruv.userservice.api.UserRole;

import java.util.Set;

public record UserInfo(String username, Set<UserRole> roles) {
}
