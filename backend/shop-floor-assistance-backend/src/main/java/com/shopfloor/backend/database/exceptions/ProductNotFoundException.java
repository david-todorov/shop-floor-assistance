package com.shopfloor.backend.database.exceptions;

/**
 * Exception thrown when the requested product is not found.
 * @author David Todorov (https://github.com/david-todorov)
 */
public class ProductNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Product not found";

    /**
     * Constructs a new ProductNotFoundException with a default message.
     */
    public ProductNotFoundException() {
        super(MESSAGE);
    }
}
