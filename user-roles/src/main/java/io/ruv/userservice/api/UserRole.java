package io.ruv.userservice.api;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public enum UserRole {
    CUSTOMER,
    ADMIN,
    COURIER;

    public static List<UserRole> tryParse(List<String> strings) {

        return strings.stream()
                .map(UserRole::tryParse)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    public static Optional<UserRole> tryParse(String string) {

        return Arrays.stream(UserRole.values())
                .filter(userRole -> userRole.name().toLowerCase().equals(string))
                .findAny();
    }
}
