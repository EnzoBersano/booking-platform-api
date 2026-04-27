package com.enzobersano.booking_platform_api.booking.api.dto;

import java.time.Instant;
import java.util.UUID;

public record BookingResponse(
        UUID id,
        UUID resourceId,
        UUID userId,
        Instant startTime,
        Instant endTime,
        String status
) {}