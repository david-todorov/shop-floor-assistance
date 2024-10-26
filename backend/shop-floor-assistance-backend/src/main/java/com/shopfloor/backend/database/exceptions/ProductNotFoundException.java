package com.shopfloor.backend.database.exceptions;

public class ProductNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Product not found";

    public ProductNotFoundException() {
        super(MESSAGE);
    }
}
