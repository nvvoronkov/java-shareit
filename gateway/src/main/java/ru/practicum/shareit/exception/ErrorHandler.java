package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.Map;


@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse validationErrorHandler(final DataNotFoundException e) {
        return new ErrorResponse("Error search", e.getMessage());
    }

    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse validationErrorHandler(final DuplicateEmailException e) {
        return new ErrorResponse("Error request", e.getMessage());
    }

    @ExceptionHandler(WrongOwnerException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleNegativeCount(final WrongOwnerException e) {
        return new ErrorResponse("Error request", e.getMessage());
    }

    @ExceptionHandler(BookingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validationErrorHandler(final BookingException e) {
        log.debug("400 BAD REQUEST {}", e.getMessage(), e);
        return new ErrorResponse("Error validate", e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> validationErrorHandler(final IllegalArgumentException e) {
        log.debug("400 BAD REQUEST {}", e.getMessage(), e);
        return Map.of("error", "Unknown state: UNSUPPORTED_STATUS");
    }

    @ExceptionHandler(BookingDataException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> notFoundBookingErrorHandler(final BookingDataException e) {
        log.debug("404 BAD REQUEST {}", e.getMessage(), e);
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> unknownErrorHandler(final SQLException e) {
        return Map.of("error", e.getMessage());
    }
}
