package com.shopfloor.backend.database.exceptions;

public class ExecutionNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Execution does not exists";
    public ExecutionNotFoundException() {
        super(MESSAGE);
    }
}
