package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

@RestControllerAdvice
public class ErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public String handlerOfValidationException(final ValidationException e) {
        return String.format("Ошибка. %s", e.getMessage());
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
