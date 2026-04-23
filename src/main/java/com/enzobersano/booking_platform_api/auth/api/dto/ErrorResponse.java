package com.enzobersano.booking_platform_api.auth.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Error payload")
public record ErrorResponse(

        @Schema(example = "Invalid credentials")
        String message
) {}