package com.shopfloor.backend.services.security.utils;

import com.shopfloor.backend.services.database.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Global exception handler for managing various exceptions in the application.
 *
 * This class is annotated with @ControllerAdvice, allowing it to handle exceptions
 * thrown by any controller in the application. It provides a centralized way to
 * manage and respond to errors with appropriate HTTP status codes and messages.
 * Each handler method constructs a `ProblemDetail` object with an HTTP status,
 * the original exception message, and a custom description to provide clarity
 * on the error encountered.
 *
 * It may be used in future it may be not
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotIdentifiableException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleIdNull(OrderNumberExistsException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setProperty("description", " Order id or order number are missing");
        return problemDetail;
    }

    @ExceptionHandler(OrderNumberExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleUserOrderNumberExists(OrderNumberExistsException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setProperty("description", "The provided order number already exist and assigned for different order");
        return problemDetail;
    }

    @ExceptionHandler(OrderNotExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleUserNotFound(OrderNotExistsException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setProperty("description", "The requested order does not exist");
        return problemDetail;
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleUserNotFound(UserNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setProperty("description", "The requested user was not found.");
        return problemDetail;
    }

    @ExceptionHandler(OrderExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleOrderAlreadyExists(OrderExistsException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setProperty("description", "The order you are trying to create already exists.");
        return problemDetail;
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ProblemDetail handleBadCredentials(BadCredentialsException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        problemDetail.setProperty("description", "The username or password is incorrect");
        return problemDetail;
    }

    @ExceptionHandler(DisabledException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ProblemDetail handleDisabledAccount(DisabledException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
        problemDetail.setProperty("description", "This account is disabled");
        return problemDetail;
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ProblemDetail handleAccessDenied(AccessDeniedException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
        problemDetail.setProperty("description", "You do not have permission to access this resource");
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ProblemDetail handleGeneralException(Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        problemDetail.setProperty("description", "An unexpected error occurred");
        return problemDetail;
    }

}
