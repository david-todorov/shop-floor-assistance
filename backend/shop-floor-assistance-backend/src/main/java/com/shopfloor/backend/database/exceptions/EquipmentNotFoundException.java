package com.shopfloor.backend.database.exceptions;

/**
 * Exception thrown when the requested equipment is not found.
 * @author David Todorov (https://github.com/david-todorov)
 */
public class EquipmentNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Equipment not found";

    /**
     * Constructs a new EquipmentNotFoundException with a default message.
     */
    public EquipmentNotFoundException() {
        super(MESSAGE);
    }
}
