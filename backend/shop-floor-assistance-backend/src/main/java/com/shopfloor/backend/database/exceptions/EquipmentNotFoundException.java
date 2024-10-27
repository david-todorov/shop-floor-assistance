package com.shopfloor.backend.database.exceptions;

public class EquipmentNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Equipment not found";

    public EquipmentNotFoundException() {
        super(MESSAGE);
    }
}
