package com.shopfloor.backend.database.exceptions;

public class DuplicatedOrderException extends RuntimeException {

    private static final String MESSAGE = "Order already exists";
    public DuplicatedOrderException() {
        super(MESSAGE);
    }
}
