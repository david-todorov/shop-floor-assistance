package com.shopfloor.backend.services.interfaces;

import com.shopfloor.backend.api.transferobjects.authentication.LoginUserRequestTO;
import com.shopfloor.backend.api.transferobjects.authentication.LoginUserResponseTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * AuthenticationService is an interface for the AuthenticationController .
 * It provides a method to authenticate a user.
 * @author David Todorov (https://github.com/david-todorov)
 */
public interface AuthenticationService {
    /**
     * Authenticates a user based on the provided login request.
     *
     * @param loginUserRequestTO the login request containing user credentials.
     * @return a ResponseEntity containing the authentication response.
     */
    ResponseEntity<LoginUserResponseTO> authenticate(@RequestBody LoginUserRequestTO loginUserRequestTO);
}

