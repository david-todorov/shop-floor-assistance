package com.uhlmann.shopfloor.shopfloorassistancebackend.api.controllers;

import com.uhlmann.shopfloor.shopfloorassistancebackend.api.dtos.AuthenticationRequestDTO;
import com.uhlmann.shopfloor.shopfloorassistancebackend.api.dtos.AuthenticationResponseDTO;
import com.uhlmann.shopfloor.shopfloorassistancebackend.security.authentication.JwtService;
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
    public ResponseEntity<AuthenticationResponseDTO> authenticate(@RequestBody AuthenticationRequestDTO authenticationRequestDto) {
        // Authenticate the user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequestDto.getUsername(), authenticationRequestDto.getPassword())
        );

        // Load user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequestDto.getUsername());

        // Generate JWT token
        String jwtToken = jwtService.generateToken(userDetails);

        // Set expiration time (e.g., 24 hours)
        long expiresIn = jwtService.getExpirationTime();
        System.out.println(expiresIn);
        // Create the response
        AuthenticationResponseDTO response = new AuthenticationResponseDTO(jwtToken, LocalDateTime.now(ZoneOffset.UTC), expiresIn);

        return ResponseEntity.ok(response);
    }
}
