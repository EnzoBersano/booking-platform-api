package com.enzobersano.booking_platform_api.auth.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Issued JWT token")
public record AuthResponse(

        @Schema(example = "eyJhbGciOiJIUzI1NiJ9...")
        String token,

        @Schema(example = "Bearer")
        String tokenType,

        @Schema(example = "USER")
        String role
) {
    public static AuthResponse of(String token, String role) {
        return new AuthResponse(token, "Bearer", role);
    }
}