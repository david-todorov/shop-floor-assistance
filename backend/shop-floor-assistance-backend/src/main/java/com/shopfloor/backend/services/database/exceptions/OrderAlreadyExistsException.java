package com.shopfloor.backend.services.database.exceptions;

public class OrderAlreadyExistsException extends RuntimeException {

    private static final String MESSAGE = "Order already exists";
    public OrderAlreadyExistsException() {
        super(MESSAGE);
    }
}
