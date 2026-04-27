package com.enzobersano.booking_platform_api.booking.application.port;

import com.enzobersano.booking_platform_api.booking.application.query.ListBookingsQuery;
import com.enzobersano.booking_platform_api.booking.domain.model.Booking;
import com.enzobersano.booking_platform_api.shared.pagination.PageResult;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepositoryPort {

    Booking save(Booking booking);

    Optional<Booking> findById(UUID bookingId);

    PageResult<Booking> findAll(ListBookingsQuery query);

    List<Booking> findByResourceId(UUID resourceId);

    List<Booking> findByResourceIdAndRange(
            UUID resourceId,
            Instant start,
            Instant end
    );

    List<Booking> findByUserId(UUID userId);
}