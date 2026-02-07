
package com.gooddeeds.backend.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

//Utility class for accessing security context information
public final class SecurityUtils {

    private SecurityUtils() {
        // Utility class - no instantiation
    }

    //Get the current authenticated user's ID.
    public static UUID getCurrentUserId() {
        return getCurrentUser().userId();
    }

    //Get the current authenticated user's email.
    public static String getCurrentEmail() {
        return getCurrentUser().email();
    }

    //Get the current authenticated user.
    public static AuthenticatedUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof AuthenticatedUser authenticatedUser) {
            return authenticatedUser;
        }

        throw new IllegalStateException("Unexpected principal type: " + principal.getClass());
    }

   //check if a user is authenticated
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        return authentication.getPrincipal() instanceof AuthenticatedUser;
    }
}
