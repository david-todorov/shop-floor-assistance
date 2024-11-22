package com.shopfloor.backend.database.exceptions;

/**
 * Exception thrown when an attempt is made to create a duplicate product.
 * @author David Todorov (https://github.com/david-todorov)
 */
public class DuplicateProductException extends RuntimeException {

    private static final String MESSAGE = "Product already exists";

    /**
     * Constructs a new DuplicateProductException with a default message.
     */
    public DuplicateProductException() {
        super(MESSAGE);
    }
}
