package com.enzobersano.booking_platform_api.booking.domain.policy;

import com.enzobersano.booking_platform_api.booking.domain.failure.BookingFailure;
import com.enzobersano.booking_platform_api.booking.domain.model.Booking;
import com.enzobersano.booking_platform_api.booking.domain.model.BookingStatus;
import com.enzobersano.booking_platform_api.shared.result.Result;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Component
public class DefaultBookingPolicy implements BookingPolicy {

    private static final Duration MIN_DURATION = Duration.ofMinutes(30);
    private static final Duration MAX_ADVANCE_WINDOW = Duration.ofDays(180);

    public Result<Void, BookingFailure> validate(
            Booking newBooking,
            List<Booking> existingBookings,
            boolean resourceActive,
            Instant now
    ) {

        var timeValidation = validateTimeRules(newBooking, now);
        if (!timeValidation.isSuccess()) {
            return timeValidation;
        }

        var resourceValidation = validateResource(resourceActive);
        if (!resourceValidation.isSuccess()) {
            return resourceValidation;
        }

        var overlapValidation = validateNoOverlap(newBooking, existingBookings);
        if (!overlapValidation.isSuccess()) {
            return overlapValidation;
        }

        var userValidation = validateUserDoubleBooking(newBooking, existingBookings);
        if (!userValidation.isSuccess()) {
            return userValidation;
        }

        return Result.success(null);
    }

    private Result<Void, BookingFailure> validateTimeRules(
            Booking booking,
            Instant now
    ) {
        var start = booking.timeRange().start();
        var end = booking.timeRange().end();

        if (!start.isAfter(now)) {
            return Result.failure(
                    new BookingFailure.InvalidTimeRange(
                            "Start time must be in the future"
                    )
            );
        }

        if (Duration.between(start, end).compareTo(MIN_DURATION) < 0) {
            return Result.failure(
                    new BookingFailure.InvalidTimeRange(
                            "Minimum booking duration is 30 minutes"
                    )
            );
        }

        if (start.isAfter(now.plus(MAX_ADVANCE_WINDOW))) {
            return Result.failure(
                    new BookingFailure.InvalidTimeRange(
                            "Booking too far in advance"
                    )
            );
        }

        return Result.success(null);
    }

    private Result<Void, BookingFailure> validateResource(
            boolean resourceActive
    ) {
        if (!resourceActive) {
            return Result.failure(
                    new BookingFailure.Conflict(
                            "",
                            "Resource is disabled"
                    )
            );
        }

        return Result.success(null);
    }

    private Result<Void, BookingFailure> validateNoOverlap(
            Booking newBooking,
            List<Booking> existingBookings
    ) {

        for (Booking existing : existingBookings) {

            if (existing.status() != BookingStatus.ACTIVE) continue;

            if (!existing.resourceId().equals(newBooking.resourceId())) continue;

            if (existing.timeRange().overlaps(newBooking.timeRange())) {
                return Result.failure(
                        new BookingFailure.Conflict(
                                newBooking.resourceId().toString(),
                                "Time range overlaps with existing booking"
                        )
                );
            }
        }

        return Result.success(null);
    }

    private Result<Void, BookingFailure> validateUserDoubleBooking(
            Booking newBooking,
            List<Booking> existingBookings
    ) {
        for (Booking existing : existingBookings) {

            if (existing.status() != BookingStatus.ACTIVE) continue;

            if (!existing.userId().equals(newBooking.userId())) continue;

            if (existing.timeRange().overlaps(newBooking.timeRange())) {
                return Result.failure(
                        new BookingFailure.Conflict(
                                newBooking.resourceId().toString(),
                                "User already has another booking in that time slot"
                        )
                );
            }
        }

        return Result.success(null);
    }
}