package com.shopfloor.backend.services.database.exceptions;

public class OrderNotIdentifiableException extends RuntimeException {

    private static final String MESSAGE = "The provided id or order number is null";
    public OrderNotIdentifiableException() {
        super(MESSAGE);
    }
}
