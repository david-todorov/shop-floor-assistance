package com.shopfloor.backend.api.controllers;

import com.shopfloor.backend.api.transferobjects.authentication.AuthenticationUserResponseTO;
import com.shopfloor.backend.api.transferobjects.authentication.LoginUserRequestTO;
import com.shopfloor.backend.services.interfaces.AuthenticationService;
import com.shopfloor.backend.services.implementations.AuthenticationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *                            PLEASE READ
 * This is a blueprint for controller which uses AuthenticationServiceImpl
 * In our case we use it through an interface called AuthenticationService
 * Any logic should be in declared in AuthenticationService interface first
 * Then AuthenticationServiceImpl implements it
 * Finally AuthenticationController uses it
 *                               IMPORTANT
 * No concrete implementations here, just use the "authenticationService"
 * This ensures that the implementation is flexible and scalable
 * Thank you for your time, now go implement
 **/
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationServiceImpl authenticationServiceImpl) {
        this.authenticationService = authenticationServiceImpl;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationUserResponseTO> authenticate(@RequestBody LoginUserRequestTO loginUserRequestTO) {
        return authenticationService.authenticate(loginUserRequestTO);
    }
}
