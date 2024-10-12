package com.uhlmann.shopfloor.shopfloorassistancebackend.api.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class AuthenticationResponseDTO {
    private String token;

    private String createdAt;

    private String expiresAt;

    public AuthenticationResponseDTO(String token, LocalDateTime createdAt, long expiresIn) {
        this.token = token;
        this.createdAt = formatDateTime(createdAt);
        this.expiresAt = formatDateTime(createdAt.plusSeconds(expiresIn / 1000));
    }

    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }
}
