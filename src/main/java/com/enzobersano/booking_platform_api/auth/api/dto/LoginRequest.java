package com.enzobersano.booking_platform_api.auth.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credentials for login")
public record LoginRequest(

        @Schema(example = "enzo@example.com")
        @NotBlank @Email
        String email,

        @Schema(example = "Secure1!")
        @NotBlank
        String password
) {}