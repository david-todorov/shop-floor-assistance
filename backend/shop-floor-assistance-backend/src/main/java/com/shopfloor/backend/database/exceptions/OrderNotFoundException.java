package com.shopfloor.backend.database.exceptions;

public class OrderNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Order does not exists";
    public OrderNotFoundException() {
        super(MESSAGE);
    }
}
