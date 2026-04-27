package com.enzobersano.booking_platform_api.booking.application.query;

import com.enzobersano.booking_platform_api.booking.domain.failure.BookingFailure;
import com.enzobersano.booking_platform_api.shared.result.Result;

public enum BookingSortBy {

    START_TIME("startTime"),
    END_TIME("endTime"),
    STATUS("status");

    private final String field;

    BookingSortBy(String field) {
        this.field = field;
    }

    public String field() {
        return field;
    }

    public static Result<BookingSortBy, BookingFailure> from(String value) {

        if (value == null || value.isBlank()) {
            return Result.failure(
                    new BookingFailure.InvalidSortBy("null or blank")
            );
        }

        return switch (value.trim().toLowerCase()) {
            case "starttime", "start_time", "start" ->
                    Result.success(START_TIME);

            case "endtime", "end_time", "end" ->
                    Result.success(END_TIME);

            case "status" ->
                    Result.success(STATUS);

            default ->
                    Result.failure(
                            new BookingFailure.InvalidSortBy(value)
                    );
        };
    }
}