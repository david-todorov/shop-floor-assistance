package com.shopfloor.backend.api.transferobjects.authentication;

import lombok.Getter;
import lombok.Setter;

/**
 * Transfer object for user login request.
 * Contains the username and password fields.
 * @author David Todorov (https://github.com/david-todorov)
 */
@Getter
@Setter
public class LoginUserRequestTO {
    /**
     * The username of the user.
     */
    private String username;

    /**
     * The password of the user.
     */
    private String password;
}
