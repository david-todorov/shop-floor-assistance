package com.shopfloor.backend.database.exceptions;

/**
 * Exception thrown when the requested user is not found.
 * @author David Todorov (https://github.com/david-todorov)
 */
public class UserNotFoundException extends RuntimeException {
    private static final String MESSAGE = "User not found";

    /**
     * Constructs a new UserNotFoundException with a default message.
     */
    public UserNotFoundException() {
        super(MESSAGE);
    }
}
