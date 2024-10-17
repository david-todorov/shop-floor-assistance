package com.shopfloor.backend.services.database.exceptions;

public class OrderNumberExistsException extends RuntimeException {

    private static final String MESSAGE = "Order number already exists";
    public OrderNumberExistsException() {
        super(MESSAGE);
    }
}
