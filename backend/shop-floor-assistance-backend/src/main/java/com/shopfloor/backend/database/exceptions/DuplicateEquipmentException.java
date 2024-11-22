package com.shopfloor.backend.database.exceptions;

/**
 * Exception thrown when an attempt is made to create a duplicate equipment.
 * @author David Todorov (https://github.com/david-todorov)
 */
public class DuplicateEquipmentException extends RuntimeException {

    private static final String MESSAGE = "Equipment already exists";

    /**
     * Constructs a new DuplicateEquipmentException with a default message.
     */
    public DuplicateEquipmentException() {
        super(MESSAGE);
    }
}
