package com.enzobersano.booking_platform_api.auth.application.port;

import com.enzobersano.booking_platform_api.auth.domain.model.User;

public interface TokenPort {
    String generateToken(User user);
    boolean validateToken(String token);
    String extractEmail(String token);
}