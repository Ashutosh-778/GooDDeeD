package com.gooddeeds.backend.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

/**
 * Utility class to access the current authenticated user from SecurityContext.
 */
public final class SecurityUtils {

    private SecurityUtils() {
        // Utility class - no instantiation
    }

    /**
     * Get the current authenticated user's ID.
     *
     * @return the UUID of the current user
     * @throws IllegalStateException if no user is authenticated
     */
    public static UUID getCurrentUserId() {
        return getCurrentUser().userId();
    }

    /**
     * Get the current authenticated user's email.
     *
     * @return the email of the current user
     * @throws IllegalStateException if no user is authenticated
     */
    public static String getCurrentEmail() {
        return getCurrentUser().email();
    }

    /**
     * Get the current authenticated user.
     *
     * @return the AuthenticatedUser principal
     * @throws IllegalStateException if no user is authenticated
     */
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

    /**
     * Check if there is a currently authenticated user.
     *
     * @return true if a user is authenticated, false otherwise
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        return authentication.getPrincipal() instanceof AuthenticatedUser;
    }
}
