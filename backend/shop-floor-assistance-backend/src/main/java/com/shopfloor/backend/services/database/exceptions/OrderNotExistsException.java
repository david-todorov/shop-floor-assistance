package com.shopfloor.backend.services.database.exceptions;

public class OrderNotExistsException extends RuntimeException {

    private static final String MESSAGE = "Order does not exists";
    public OrderNotExistsException() {
        super(MESSAGE);
    }
}
