package com.shopfloor.backend.database.exceptions;

public class MissingOrderException extends RuntimeException {

    private static final String MESSAGE = "The provided order is null";
    public MissingOrderException() {
        super(MESSAGE);
    }
}
