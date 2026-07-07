package br.com.gabifontainhas.techchallenge.infrastructure.exception;

import br.com.gabifontainhas.techchallenge.application.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handle(UserNotFoundException ex) {
        var problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        return problem;
    }

    @ExceptionHandler(RestaurantNotFoundException.class)
    public ProblemDetail handle(RestaurantNotFoundException ex) {
        var problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        return problem;
    }

    @ExceptionHandler(MenuItemNotFoundException.class)
    public ProblemDetail handle(MenuItemNotFoundException ex) {
        var problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        return problem;
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ProblemDetail handle(EmailAlreadyExistsException ex) {
        var problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNPROCESSABLE_CONTENT,
                ex.getMessage()
        );
        return problem;
    }

    @ExceptionHandler(RestaurantAlreadyExistsException.class)
    public ProblemDetail handle(RestaurantAlreadyExistsException ex) {
        var problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNPROCESSABLE_CONTENT,
                ex.getMessage()
        );
        return problem;
    }

    @ExceptionHandler(MenuItemAlreadyExistsException.class)
    public ProblemDetail handle(MenuItemAlreadyExistsException ex) {
        var problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNPROCESSABLE_CONTENT,
                ex.getMessage()
        );
        return problem;
    }

}

