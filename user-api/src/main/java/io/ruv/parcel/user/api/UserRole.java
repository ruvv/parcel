package io.ruv.parcel.user.api;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum UserRole {
    CUSTOMER,
    ADMIN,
    COURIER;

    public static Set<UserRole> tryParse(String[] strings) {

        return tryParse(Arrays.stream(strings));
    }

    public static Set<UserRole> tryParse(List<String> strings) {

        return tryParse(strings.stream());
    }

    private static Set<UserRole> tryParse(Stream<String> strings) {

        return strings.map(UserRole::tryParse)
                .flatMap(Optional::stream)
                .collect(Collectors.toSet());
    }

    public static Optional<UserRole> tryParse(String string) {

        return Arrays.stream(UserRole.values())
                .filter(userRole -> userRole.name().equalsIgnoreCase(string))
                .findAny();
    }
}
