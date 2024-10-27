package com.shopfloor.backend.database.exceptions;

public class DuplicateEquipmentException extends RuntimeException {

    private static final String MESSAGE = "Equipment already exists";
    public DuplicateEquipmentException() {
        super(MESSAGE);
    }
}
