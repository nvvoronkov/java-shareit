package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public String handlerOfValidationException(final ValidationException e) {
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UnsupportedStatusException.class)
    public Error handlerOfUnsupportedStatusException(final UnsupportedStatusException e) {
        return new Error(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public Error handlerOfIllegalArgumentException(final IllegalArgumentException e) {
        return new Error("Unknown state: UNSUPPORTED_STATUS");
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handlerOfConstraintValidationException(final ConstraintViolationException e) {
        return String.format("Ошибка. %s", e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ObjectNotFoundException.class)
    public String handlerOfObjectNotFoundException(final ObjectNotFoundException e) {
        return String.format("Ошибка. %s", e.getMessage());
    }

    @ExceptionHandler(ConflictErrorException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handlerOfConflictErrorException(final ConflictErrorException e) {
        return String.format("Ошибка. %s", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handlerExceptions(final Exception e) {
        return String.format("Ошибка. %s", e.getMessage());
    }
}
