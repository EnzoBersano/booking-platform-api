package com.enzobersano.booking_platform_api.booking.policy;

import com.enzobersano.booking_platform_api.booking.domain.failure.BookingFailure;
import com.enzobersano.booking_platform_api.booking.domain.model.Booking;
import com.enzobersano.booking_platform_api.booking.domain.model.BookingStatus;
import com.enzobersano.booking_platform_api.booking.domain.policy.DefaultBookingPolicy;
import com.enzobersano.booking_platform_api.booking.domain.valueobject.TimeRange;
import com.enzobersano.booking_platform_api.shared.result.Result;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DefaultBookingPolicyTest {

    private final DefaultBookingPolicy policy = new DefaultBookingPolicy();

    @Test
    void shouldFailWhenResourceIsInactive() {


        var now = Instant.parse("2026-01-01T10:00:00Z");

        var timeRange = new TimeRange(
                now.plusSeconds(3600),
                now.plusSeconds(7200)
        );

        var booking = Booking.reconstitute(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                timeRange,
                BookingStatus.ACTIVE
        );


        var result = policy.validate(
                booking,
                List.of(),
                false, // resource inactive
                now
        );


        assertFalse(result.isSuccess());
        assertInstanceOf(BookingFailure.Conflict.class, result.error());
    }

    @Test
    void shouldApproveValidBooking() {


        var now = Instant.parse("2026-01-01T10:00:00Z");

        var timeRange = new TimeRange(
                now.plusSeconds(3600), // +1h
                now.plusSeconds(7200)  // +2h (duration = 1h > 30min)
        );

        var booking = Booking.reconstitute(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                timeRange,
                BookingStatus.ACTIVE
        );

        List<Booking> existingBookings = List.of();


        Result<Void, BookingFailure> result = policy.validate(
                booking,
                existingBookings,
                true, // resource active
                now
        );


        assertTrue(result.isSuccess());
        assertNull(result.value());
    }
}
