package com.enzobersano.booking_platform_api.booking.application.command;

import java.time.Instant;
import java.util.UUID;

public record CreateBookingCommand(
        UUID resourceId,
        UUID userId,
        Instant startTime,
        Instant endTime
) {}