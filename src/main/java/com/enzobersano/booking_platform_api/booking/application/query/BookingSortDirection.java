package com.enzobersano.booking_platform_api.booking.application.query;

import com.enzobersano.booking_platform_api.booking.domain.failure.BookingFailure;
import com.enzobersano.booking_platform_api.shared.result.Result;

public enum BookingSortDirection {
    ASC,
    DESC;

    public static Result<BookingSortDirection, BookingFailure> from(String value) {

        if (value == null || value.isBlank()) {
            return Result.failure(
                    new BookingFailure.InvalidSortDirection("null or blank")
            );
        }

        return switch (value.trim().toLowerCase()) {
            case "asc" -> Result.success(ASC);
            case "desc" -> Result.success(DESC);
            default ->
                    Result.failure(
                            new BookingFailure.InvalidSortDirection(value)
                    );
        };
    }
}