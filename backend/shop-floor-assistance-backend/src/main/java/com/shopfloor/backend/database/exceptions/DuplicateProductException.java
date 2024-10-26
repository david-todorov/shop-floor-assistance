package com.shopfloor.backend.database.exceptions;

public class DuplicateProductException extends RuntimeException {

    private static final String MESSAGE = "Product already exists";
    public DuplicateProductException() {
        super(MESSAGE);
    }
}
