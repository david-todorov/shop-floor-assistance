package com.shopfloor.backend.security;

import com.shopfloor.backend.database.objects.UserDBO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utility class for retrieving authenticated user details.
 * @author David Todorov (https://github.com/david-todorov)
 */
public class AuthenticatedUserDetails {

    /**
     * Retrieves the ID of the currently authenticated user.
     *
     * @return the ID of the currently authenticated user
     * @throws RuntimeException if the user is not authenticated
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDBO userDetails = (UserDBO) authentication.getPrincipal(); // Cast to UserDBO
            return userDetails.getId();
        }
        throw new RuntimeException("User is not authenticated");
    }

}
