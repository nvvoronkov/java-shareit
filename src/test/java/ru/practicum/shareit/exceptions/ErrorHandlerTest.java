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
        Assertions.assertEquals(message.getError(),"Ошибка валидации");
    }

    @Test
    void handlerOfConstraintValidationException() {
        ErrorResponse errorResponse = errorHandler.validationErrorHandler(new ConstraintViolationException("message",
                Collections.emptySet()));
        Assertions.assertEquals(errorResponse.getError(), "Ошибка валидации");
    }

    @Test
    void handlerOfObjectNotFoundException() {
        ErrorResponse errorResponse = errorHandler.validationErrorHandler(new DataNotFoundException("message"));
        Assertions.assertEquals(errorResponse.getError(), "Ошибка поиска");
    }

    @Test
    void handlerExceptions() {
        ErrorResponse errorResponse = errorHandler.handleNegativeCount(new WrongOwnerException("message"));
        Assertions.assertEquals(errorResponse.getError(), "Ошибка запроса");
    }
}