package com.enzobersano.booking_platform_api.booking.domain.failure;

import com.enzobersano.booking_platform_api.shared.result.Failure;

public sealed interface BookingFailure extends Failure
        permits BookingFailure.Conflict, BookingFailure.InvalidSortBy, BookingFailure.InvalidSortDirection, BookingFailure.InvalidTimeRange, BookingFailure.NotFound {

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

    record InvalidSortBy(String value) implements BookingFailure {
        @Override
        public String message() {
            return "Invalid sort field: " + value;
        }
    }

    record InvalidSortDirection(String value) implements BookingFailure {
        @Override
        public String message() {
            return "Invalid sort direction: " + value + ". Allowed: asc, desc";
        }
    }
}