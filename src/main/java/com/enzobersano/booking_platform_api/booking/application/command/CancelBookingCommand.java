package com.enzobersano.booking_platform_api.booking.application.command;

import java.util.UUID;

public record CancelBookingCommand(
        UUID bookingId
) {}