package com.shopfloor.backend;

import com.shopfloor.backend.database.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
 * @author David Todorov (https://github.com/david-todorov)
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles ExecutionNotFoundException.
     *
     * @param ex the exception
     * @return a ProblemDetail object with NOT_FOUND status and a description
     */
    @ExceptionHandler(ExecutionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleExecutionNotFound(ExecutionNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setProperty("description", "The requested execution was not found.");
        return problemDetail;
    }

    /**
     * Handles EquipmentNotFoundException.
     *
     * @param ex the exception
     * @return a ProblemDetail object with NOT_FOUND status and a description
     */
    @ExceptionHandler(EquipmentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleEquipmentNotFound(EquipmentNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setProperty("description", "The requested equipment was not found.");
        return problemDetail;
    }

    /**
     * Handles DuplicateEquipmentException.
     *
     * @param ex the exception
     * @return a ProblemDetail object with CONFLICT status and a description
     */
    @ExceptionHandler(DuplicateEquipmentException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleEquipmentAlreadyExists(DuplicateEquipmentException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setProperty("description", "The equipment you are trying to create already exists.");
        return problemDetail;
    }

    /**
     * Handles DuplicateProductException.
     *
     * @param ex the exception
     * @return a ProblemDetail object with CONFLICT status and a description
     */
    @ExceptionHandler(DuplicateProductException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleProductAlreadyExists(DuplicateProductException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setProperty("description", "The product you are trying to create already exists.");
        return problemDetail;
    }

    /**
     * Handles ProductNotFoundException.
     *
     * @param ex the exception
     * @return a ProblemDetail object with NOT_FOUND status and a description
     */
    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleProductNotFound(ProductNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setProperty("description", "The requested product was not found.");
        return problemDetail;
    }

    /**
     * Handles HttpMessageNotReadableException.
     *
     * @param ex the exception
     * @return a ProblemDetail object with BAD_REQUEST status and a description
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleRequestBodyIsNull(HttpMessageNotReadableException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Request body is missing or invalid");
        problemDetail.setProperty("description", "The request body must not be null or malformed.");
        problemDetail.setProperty("error", "Request body is null or could not be read.");
        return problemDetail;
    }

    /**
     * Handles MethodArgumentNotValidException.
     *
     * @param ex the exception
     * @return a ProblemDetail object with BAD_REQUEST status and a description
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
        problemDetail.setProperty("description", "Input validation failed for the provided data.");
        return problemDetail;
    }

    /**
     * Handles OrderNotFoundException.
     *
     * @param ex the exception
     * @return a ProblemDetail object with NOT_FOUND status and a description
     */
    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleUserNotFound(OrderNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setProperty("description", "The requested order does not exist");
        return problemDetail;
    }

    /**
     * Handles DuplicatedOrderException.
     *
     * @param ex the exception
     * @return a ProblemDetail object with CONFLICT status and a description
     */
    @ExceptionHandler(DuplicatedOrderException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleOrderAlreadyExists(DuplicatedOrderException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setProperty("description", "The order you are trying to create already exists.");
        return problemDetail;
    }

    /**
     * Handles BadCredentialsException.
     *
     * @param ex the exception
     * @return a ProblemDetail object with UNAUTHORIZED status and a description
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ProblemDetail handleBadCredentials(BadCredentialsException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        problemDetail.setProperty("description", "The username or password is incorrect");
        return problemDetail;
    }

}
