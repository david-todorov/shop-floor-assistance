package com.shopfloor.backend.services.implementations;

import com.shopfloor.backend.api.transferobjects.authentication.LoginUserRequestTO;
import com.shopfloor.backend.api.transferobjects.authentication.LoginUserResponseTO;
import com.shopfloor.backend.database.objects.UserDBO;
import com.shopfloor.backend.security.JwtService;
import com.shopfloor.backend.services.interfaces.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Implementation of the AuthenticationService interface.
 * Provides methods to authenticate users and generate JWT tokens.
 * @author David Todorov (https://github.com/david-todorov)
 */
@Component
public class AuthenticationServiceImpl implements AuthenticationService {

    /**
     * The authentication manager to authenticate users.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * The user details tests to load user information.
     */
    private final UserDetailsService userDetailsService;

    /**
     * The JWT to generate tokens.
     */
    private final JwtService jwtService;

    /**
     * Constructs an AuthenticationServiceImpl with the specified dependencies.
     *
     * @param authenticationManager the authentication manager to authenticate users
     * @param userDetailsService the user details tests to load user information
     * @param jwtService the JWT tests to generate tokens
     */
    @Autowired
    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    /**
     * Authenticates a user based on the provided login request.
     *
     * @param loginUserRequestTO the login request transfer object containing username and password
     * @return a ResponseEntity containing the authentication response transfer object with the JWT token and expiration time
     */
    @Override
    public ResponseEntity<LoginUserResponseTO> authenticate(@RequestBody LoginUserRequestTO loginUserRequestTO) {
        // Authenticate the user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUserRequestTO.getUsername(), loginUserRequestTO.getPassword())
        );

        // Load user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginUserRequestTO.getUsername());

        // Retrieve the user ID from the userDetails object (assuming you have a method to get it)
        Long userId = ((UserDBO) userDetails).getId(); // Assuming CustomUserDetails implements UserDetails and has getId()

        // Generate JWT token
        String jwtToken = jwtService.generateToken(userId, userDetails);

        // Set expiration time (e.g., 24 hours)
        long expiresIn = jwtService.getExpirationTime();

        // Create the response
        LoginUserResponseTO response = new LoginUserResponseTO(jwtToken, LocalDateTime.now(ZoneOffset.UTC), expiresIn);

        return ResponseEntity.ok(response);
    }
}
