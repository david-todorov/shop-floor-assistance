package com.shopfloor.backend.database.exceptions;

/**
 * Exception thrown when the requested execution is not found.
 * @author David Todorov (https://github.com/david-todorov)
 */
public class ExecutionNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Execution does not exists";

    /**
     * Constructs a new ExecutionNotFoundException with a default message.
     */
    public ExecutionNotFoundException() {
        super(MESSAGE);
    }
}
