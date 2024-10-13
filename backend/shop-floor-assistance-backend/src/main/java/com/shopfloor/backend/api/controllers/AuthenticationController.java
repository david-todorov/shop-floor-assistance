package com.shopfloor.backend.api.controllers;

import com.shopfloor.backend.api.transferobjects.AuthenticationUserResponseTO;
import com.shopfloor.backend.api.transferobjects.LoginUserRequestTO;
import com.shopfloor.backend.services.security.authentication.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 *                            PLEASE READ
 * This is an AuthenticationController that authenticate users
 * User sends a "LoginUserRequestTO" in a form of JSON
 * The server sends back "AuthenticationUserResponseTO" in a form of JSON
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    UserDetailsService userDetailsService,
                                    JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationUserResponseTO> authenticate(@RequestBody LoginUserRequestTO loginUserRequestTO) {
        // Authenticate the user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUserRequestTO.getUsername(), loginUserRequestTO.getPassword())
        );

        // Load user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginUserRequestTO.getUsername());

        // Generate JWT token
        String jwtToken = jwtService.generateToken(userDetails);

        // Set expiration time (e.g., 24 hours)
        long expiresIn = jwtService.getExpirationTime();
        System.out.println(expiresIn);
        // Create the response
        AuthenticationUserResponseTO response = new AuthenticationUserResponseTO(jwtToken, LocalDateTime.now(ZoneOffset.UTC), expiresIn);

        return ResponseEntity.ok(response);
    }
}
