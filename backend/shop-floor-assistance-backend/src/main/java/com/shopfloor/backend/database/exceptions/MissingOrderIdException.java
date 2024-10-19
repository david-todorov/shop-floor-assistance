package com.shopfloor.backend.database.exceptions;

public class MissingOrderIdException extends RuntimeException {

    private static final String MESSAGE = "The provided id is null";
    public MissingOrderIdException() {
        super(MESSAGE);
    }
}
