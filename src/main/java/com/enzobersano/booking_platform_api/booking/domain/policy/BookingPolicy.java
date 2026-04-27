package com.enzobersano.booking_platform_api.booking.domain.policy;

import com.enzobersano.booking_platform_api.booking.domain.failure.BookingFailure;
import com.enzobersano.booking_platform_api.booking.domain.model.Booking;
import com.enzobersano.booking_platform_api.shared.result.Result;

import java.time.Instant;
import java.util.List;

public interface BookingPolicy {

    Result<Void, BookingFailure> validate(
            Booking newBooking,
            List<Booking> existingBookings,
            boolean resourceActive,
            Instant now
    );
}