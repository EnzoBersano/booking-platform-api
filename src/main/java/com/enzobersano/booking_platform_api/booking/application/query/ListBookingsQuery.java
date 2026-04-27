package com.enzobersano.booking_platform_api.booking.application.query;

public record ListBookingsQuery(
        int page,
        int size,
        BookingSortBy sortBy,
        BookingSortDirection direction
) {}