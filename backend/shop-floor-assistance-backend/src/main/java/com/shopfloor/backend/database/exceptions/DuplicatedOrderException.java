package com.shopfloor.backend.database.exceptions;

/**
 * Exception thrown when an attempt is made to create a duplicate order.
 * @author David Todorov (https://github.com/david-todorov)
 */
public class DuplicatedOrderException extends RuntimeException {

    private static final String MESSAGE = "Order already exists";

    /**
     * Constructs a new DuplicatedOrderException with a default message.
     */
    public DuplicatedOrderException() {
        super(MESSAGE);
    }
}
