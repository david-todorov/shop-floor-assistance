package com.shopfloor.backend.security;

import com.shopfloor.backend.database.objects.UserDBO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticatedUserDetails {

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDBO userDetails = (UserDBO) authentication.getPrincipal(); // Cast to UserDBO
            return userDetails.getId();
        }
        throw new RuntimeException("User is not authenticated");
    }

    public static String  getCurrentUserUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDBO userDetails = (UserDBO) authentication.getPrincipal(); // Cast to UserDBO
            return userDetails.getUsername();
        }
        throw new RuntimeException("User is not authenticated");
    }
}
