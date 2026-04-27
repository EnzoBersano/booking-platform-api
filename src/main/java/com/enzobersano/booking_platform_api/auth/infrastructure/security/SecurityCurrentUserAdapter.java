package com.enzobersano.booking_platform_api.auth.infrastructure.security;

import com.enzobersano.booking_platform_api.auth.application.port.CurrentUserPort;
import com.enzobersano.booking_platform_api.auth.domain.model.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SecurityCurrentUserAdapter implements CurrentUserPort {

    @Override
    public UUID getCurrentUserId() {

        var authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new IllegalStateException("No authenticated user");
        }

        var principal = authentication.getPrincipal();

        if (!(principal instanceof User user)) {
            throw new IllegalStateException("Invalid authenticated principal");
        }

        return user.id();
    }
}