package com.enzobersano.booking_platform_api.booking.infrastructure.persistence;

import com.enzobersano.booking_platform_api.booking.application.port.BookingRepositoryPort;
import com.enzobersano.booking_platform_api.booking.application.query.ListBookingsQuery;
import com.enzobersano.booking_platform_api.booking.domain.model.Booking;
import com.enzobersano.booking_platform_api.booking.application.query.BookingSortDirection;
import com.enzobersano.booking_platform_api.booking.infrastructure.mapper.BookingMapper;
import com.enzobersano.booking_platform_api.shared.infrastructure.persistence.PageableFactory;
import com.enzobersano.booking_platform_api.shared.pagination.PageResult;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class BookingRepositoryAdapter implements BookingRepositoryPort {

    private final BookingJpaRepository jpa;
    private final BookingMapper mapper;

    public BookingRepositoryAdapter(
            BookingJpaRepository jpa,
            BookingMapper mapper
    ) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public Booking save(Booking booking) {
        return mapper.toDomain(
                jpa.save(
                        mapper.toEntity(booking)
                )
        );
    }

    @Override
    public Optional<Booking> findById(UUID id) {
        return jpa.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Booking> findByResourceId(UUID resourceId) {
        return jpa.findByResourceId(resourceId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Booking> findByResourceIdAndRange(
            UUID resourceId,
            Instant start,
            Instant end
    ) {
        return jpa.findOverlappingBookings(resourceId, start, end)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Booking> findByUserId(UUID userId) {
        return jpa.findByUserId(userId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public PageResult<Booking> findAll(ListBookingsQuery query) {

        var pageable = PageableFactory.create(
                query.page(),
                query.size(),
                query.sortBy().field(),
                query.direction() == BookingSortDirection.DESC
        );

        var result = jpa.findAll(pageable);

        return mapper.toPageResult(result);
    }
}