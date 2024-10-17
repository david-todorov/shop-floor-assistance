package com.shopfloor.backend.services.database.exceptions;

public class OrderExistsException extends RuntimeException {

    private static final String MESSAGE = "Order already exists";
    public OrderExistsException() {
        super(MESSAGE);
    }
}
