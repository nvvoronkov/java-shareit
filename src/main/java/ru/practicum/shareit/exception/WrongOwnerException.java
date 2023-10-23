package ru.practicum.shareit.exception;

public class WrongOwnerException extends RuntimeException {
    public WrongOwnerException() {
        super();
    }

    public WrongOwnerException(String message) {
        super(message);
    }

    public WrongOwnerException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongOwnerException(Throwable cause) {
        super(cause);
    }
}
