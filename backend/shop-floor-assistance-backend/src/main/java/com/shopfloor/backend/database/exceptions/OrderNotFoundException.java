package com.shopfloor.backend.database.exceptions;

/**
 * Exception thrown when the requested order is not found.
 * @author David Todorov (https://github.com/david-todorov)
 */
public class OrderNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Order does not exists";

    /**
     * Constructs a new OrderNotFoundException with a default message.
     */
    public OrderNotFoundException() {
        super(MESSAGE);
    }
}
