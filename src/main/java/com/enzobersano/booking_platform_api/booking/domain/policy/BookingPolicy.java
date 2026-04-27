package com.enzobersano.booking_platform_api.booking.domain.policy;

import com.enzobersano.booking_platform_api.booking.domain.failure.BookingFailure;
import com.enzobersano.booking_platform_api.booking.domain.model.Booking;
import com.enzobersano.booking_platform_api.shared.result.Result;

import java.util.List;

public class BookingPolicy {

    public static Result<Void, BookingFailure> validateNoOverlap(
            Booking newBooking,
            List<Booking> existingBookings
    ) {

        for (Booking existing : existingBookings) {

            if (!existing.resourceId().equals(newBooking.resourceId())) {
                continue;
            }

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
}