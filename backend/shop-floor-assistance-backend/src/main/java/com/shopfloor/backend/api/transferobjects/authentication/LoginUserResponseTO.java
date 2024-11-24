package com.shopfloor.backend.api.transferobjects.authentication;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Transfer object for authentication user response.
 * Contains the JWT token, creation time, and expiration time.
 * @author David Todorov (https://github.com/david-todorov)
 */
@Getter
@Setter
public class LoginUserResponseTO {
    /**
     * The JWT token.
     */
    private String token;

    /**
     * The creation time of the token.
     */
    private String createdAt;

    /**
     * The expiration time of the token.
     */
    private String expiresAt;

    /**
     * Constructs an AuthenticationUserResponseTO with the specified token, creation time, and expiration duration.
     *
     * @param token the JWT token
     * @param createdAt the creation time of the token
     * @param expiresIn the duration in seconds until the token expires
     */
    public LoginUserResponseTO(String token, LocalDateTime createdAt, long expiresIn) {
        this.token = token;
        this.createdAt = formatDateTime(createdAt);
        this.expiresAt = formatDateTime(createdAt.plusSeconds(expiresIn / 1000));
    }

    /**
     * Formats a LocalDateTime object to a string with the pattern "yyyy-MM-dd HH:mm:ss".
     *
     * @param dateTime the LocalDateTime object to format
     * @return the formatted date-time string
     */
    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }
}
