package ru.practicum.shareit.exceptions;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.*;

import javax.validation.ConstraintViolationException;
import java.util.Collections;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ErrorHandlerTest {
    private final ErrorHandler errorHandler;

    @Test
    void handlerOfValidationException() {
        ErrorResponse message = errorHandler.validationErrorHandler(new BookingException("message"));
        Assertions.assertEquals(message.getError(),"Error validate");
    }

    @Test
    void handlerOfConstraintValidationException() {
        ErrorResponse errorResponse = errorHandler.validationErrorHandler(new ConstraintViolationException("message",
                Collections.emptySet()));
        Assertions.assertEquals(errorResponse.getError(), "Error validate");
    }

    @Test
    void handlerOfObjectNotFoundException() {
        ErrorResponse errorResponse = errorHandler.validationErrorHandler(new DataNotFoundException("message"));
        Assertions.assertEquals(errorResponse.getError(), "Error search");
    }

    @Test
    void handlerExceptions() {
        ErrorResponse errorResponse = errorHandler.handleNegativeCount(new WrongOwnerException("message"));
        Assertions.assertEquals(errorResponse.getError(), "Error request");
    }

    @Test
    void handlerExceptionsEmail() {
        ErrorResponse errorResponse = errorHandler.validationErrorHandler(new DuplicateEmailException("message"));
        Assertions.assertEquals(errorResponse.getError(), "Error request");
    }
}