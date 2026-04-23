package com.enzobersano.booking_platform_api.auth.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload for new user registration")
public record RegisterRequest(

        @Schema(example = "enzo@example.com")
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @Schema(example = "Secure1!")
        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        String password,

        @Schema(example = "USER", allowableValues = {"USER", "ADMIN"})
        String role
) {}