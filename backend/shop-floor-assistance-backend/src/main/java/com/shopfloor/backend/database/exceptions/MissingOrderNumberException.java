package com.shopfloor.backend.database.exceptions;

public class MissingOrderNumberException extends RuntimeException {

    private static final String MESSAGE = "The provided order number is null";
    public MissingOrderNumberException() {
        super(MESSAGE);
    }
}
