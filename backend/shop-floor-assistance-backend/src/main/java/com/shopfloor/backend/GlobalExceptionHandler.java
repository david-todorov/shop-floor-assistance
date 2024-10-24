package com.shopfloor.backend;

import com.shopfloor.backend.database.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleRequestBodyIsNull(HttpMessageNotReadableException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Request body is missing or invalid");
        problemDetail.setProperty("description", "The request body must not be null or malformed.");

        // Optionally, you can include more details if needed (e.g., the error message)
        problemDetail.setProperty("error", "Request body is null or could not be read.");

        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
        problemDetail.setProperty("description", "Input validation failed for the provided data.");

        // Collect detailed validation error messages
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> {
                    String field = fieldError.getField();
                    String message = fieldError.getDefaultMessage();

                    // Customize messages for specific fields
                    if ("timeRequired".equals(field) && "must be greater than or equal to 1".equals(message)) {
                        message = "Field 'timeRequired' must be greater than or equal to 1.";
                    } else if ("must not be empty".equals(message)) {
                        message = "Field '" + field + "' must not be null or empty.";
                    }

                    return String.format("Field '%s': %s", field, message);
                })
                .collect(Collectors.toList());

        // Include the errors in the response
        problemDetail.setProperty("errors", errors);

        return problemDetail;
    }

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleUserNotFound(OrderNotFoundException ex) {
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

    @ExceptionHandler(DuplicatedOrderException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleOrderAlreadyExists(DuplicatedOrderException ex) {
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
