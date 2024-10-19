package com.shopfloor.backend.services.database.exceptions;

public class MissingOrderIdException extends RuntimeException {

    private static final String MESSAGE = "The provided id is null";
    public MissingOrderIdException() {
        super(MESSAGE);
    }
}
