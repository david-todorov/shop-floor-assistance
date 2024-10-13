package com.shopfloor.backend.services.security;

import com.shopfloor.backend.api.transferobjects.AuthenticationUserResponseTO;
import com.shopfloor.backend.api.transferobjects.LoginUserRequestTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * This is where all needed public methods should be declared
 * Think what AuthenticationController would need and declare it here
 * Be generic not concrete
 * Rely on actions not implementations
 * Keep the number of methods low as possible and if
 * something does not feel right,
 * probably should be private and not here
 * Have fun
 */
public interface AuthenticationService {
    ResponseEntity<AuthenticationUserResponseTO> authenticate(@RequestBody LoginUserRequestTO loginUserRequestTO);
}
