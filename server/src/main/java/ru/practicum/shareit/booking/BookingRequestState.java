package ru.practicum.shareit.booking;

import java.util.Optional;

public enum BookingRequestState {
    ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED;

    public static Optional<BookingRequestState> from(String value) {
        return Optional.ofNullable(value)
                .map(v -> {
                    try {
                        return BookingRequestState.valueOf(v.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        return null;
                    }
                });
    }
}
