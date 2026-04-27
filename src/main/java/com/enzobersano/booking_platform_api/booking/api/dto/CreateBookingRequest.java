package com.enzobersano.booking_platform_api.booking.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

@Schema(description = "Payload to create a booking")
public record CreateBookingRequest(

        @Schema(example = "550e8400-e29b-41d4-a716-446655440000")
        @NotNull
        UUID resourceId,

        @Schema(example = "2026-05-10T10:00:00Z")
        @NotNull
        Instant startTime,

        @Schema(example = "2026-05-10T11:00:00Z")
        @NotNull
        Instant endTime
) {}