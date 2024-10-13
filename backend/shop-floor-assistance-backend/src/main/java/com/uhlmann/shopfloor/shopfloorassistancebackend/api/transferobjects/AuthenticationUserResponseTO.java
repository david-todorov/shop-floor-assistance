package com.uhlmann.shopfloor.shopfloorassistancebackend.api.transferobjects;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents the response sent to the client during authentication.
 * The TO is used for transferring data to the client without being stored
 * directly in the database.
 */
@Getter
@Setter
public class AuthenticationUserResponseTO {
    private String token;

    private String createdAt;

    private String expiresAt;

    public AuthenticationUserResponseTO(String token, LocalDateTime createdAt, long expiresIn) {
        this.token = token;
        this.createdAt = formatDateTime(createdAt);
        this.expiresAt = formatDateTime(createdAt.plusSeconds(expiresIn / 1000));
    }

    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }
}
