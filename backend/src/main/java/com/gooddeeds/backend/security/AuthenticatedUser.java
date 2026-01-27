package com.gooddeeds.backend.security;

import java.util.UUID;

/**
 * Principal object stored in SecurityContext after JWT authentication.
 * Contains the user's ID and email extracted from the JWT token.
 */
public record AuthenticatedUser(UUID userId, String email) {
}
