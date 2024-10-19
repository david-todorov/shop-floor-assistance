package com.shopfloor.backend.database.exceptions;

public class MissingAuthorizationHeaderException extends RuntimeException {

    private static final String MESSAGE = "The provided authorization header is null";
    public MissingAuthorizationHeaderException() {
        super(MESSAGE);
    }
}
