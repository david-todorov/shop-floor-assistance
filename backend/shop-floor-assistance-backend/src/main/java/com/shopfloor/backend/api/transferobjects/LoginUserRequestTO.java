package com.shopfloor.backend.api.transferobjects;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents the request sent to the server from the client during authentication.
 * The TO is used for transferring data to the client without being stored
 * directly in the database.
 */
@Getter
@Setter
public class LoginUserRequestTO {
    private String username;
    private String password;
}
