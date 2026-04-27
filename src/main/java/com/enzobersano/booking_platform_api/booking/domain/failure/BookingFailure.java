package com.enzobersano.booking_platform_api.booking.domain.failure;

import com.enzobersano.booking_platform_api.shared.result.Failure;

public sealed interface BookingFailure extends Failure
        permits BookingFailure.NotFound,
        BookingFailure.InvalidTimeRange,
        BookingFailure.Conflict {

    record NotFound() implements BookingFailure {
        @Override
        public String message() {
            return "Booking not found";
        }
    }

    record InvalidTimeRange(String reason) implements BookingFailure {
        @Override
        public String message() {
            return "Invalid booking time range: " + reason;
        }
    }

    record Conflict(String resourceId, String reason) implements BookingFailure {
        @Override
        public String message() {
            return "Booking conflict for resource " + resourceId + ": " + reason;
        }
    }
}