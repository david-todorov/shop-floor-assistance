package com.shopfloor.backend.services.implementations;

import com.shopfloor.backend.api.transferobjects.authentication.AuthenticationUserResponseTO;
import com.shopfloor.backend.api.transferobjects.authentication.LoginUserRequestTO;
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
 * This is where the concrete implementations of AuthenticationService is
 * Of course implement all the needed methods
 * But do not be afraid to extract common logic in private methods
 * The number of public methods should be the same as in AuthenticationService
 * If you implement something public here and NOT declare it in AuthenticationService
 * The controller AuthenticationController can NOT see it
 * Have fun
 */
@Component
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Autowired
    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Override
    public ResponseEntity<AuthenticationUserResponseTO> authenticate(@RequestBody LoginUserRequestTO loginUserRequestTO) {
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
        AuthenticationUserResponseTO response = new AuthenticationUserResponseTO(jwtToken, LocalDateTime.now(ZoneOffset.UTC), expiresIn);

        return ResponseEntity.ok(response);
    }
}
