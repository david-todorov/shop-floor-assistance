package com.uhlmann.shopfloor.shopfloorassistancebackend.api.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationRequestDTO {
    private String username;
    private String password;
}
