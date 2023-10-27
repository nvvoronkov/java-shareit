package ru.practicum.shareit.exception;

public class BookingDataException extends RuntimeException {

    public BookingDataException() {
        super();
    }

    public BookingDataException(String message) {
        super(message);
    }

    public BookingDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookingDataException(Throwable cause) {
        super(cause);
    }
}
