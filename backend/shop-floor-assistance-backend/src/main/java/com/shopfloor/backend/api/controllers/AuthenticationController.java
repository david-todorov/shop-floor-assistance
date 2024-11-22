package com.shopfloor.backend.api.controllers;

import com.shopfloor.backend.api.transferobjects.authentication.AuthenticationUserResponseTO;
import com.shopfloor.backend.api.transferobjects.authentication.LoginUserRequestTO;
import com.shopfloor.backend.services.implementations.AuthenticationServiceImpl;
import com.shopfloor.backend.services.interfaces.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthenticationController is a REST controller that handles authentication-related requests.
 * It provides endpoints for user login and other authentication operations.
 *
 * This controller uses the AuthenticationService to perform the actual authentication logic.
 * @author David Todorov (https://github.com/david-todorov)
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Constructs an AuthenticationController with the specified AuthenticationService implementation.
     *
     * @param authenticationServiceImpl the implementation of AuthenticationService to use
     */
    @Autowired
    public AuthenticationController(AuthenticationServiceImpl authenticationServiceImpl) {
        this.authenticationService = authenticationServiceImpl;
    }

    /**
     * Authenticates a user based on the provided login request.
     *
     * @param loginUserRequestTO the login request transfer object containing user credentials
     * @return a ResponseEntity containing the authentication response transfer object
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationUserResponseTO> authenticate(@RequestBody LoginUserRequestTO loginUserRequestTO) {
        return authenticationService.authenticate(loginUserRequestTO);
    }
}
