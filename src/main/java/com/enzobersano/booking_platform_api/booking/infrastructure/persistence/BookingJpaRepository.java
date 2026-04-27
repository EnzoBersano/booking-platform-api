package com.enzobersano.booking_platform_api.booking.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


public interface BookingJpaRepository extends JpaRepository<BookingJpaEntity, UUID> {

    List<BookingJpaEntity> findByResourceId(UUID resourceId);

    List<BookingJpaEntity> findByUserId(UUID userId);

    @Query("""
        SELECT b
        FROM BookingJpaEntity b
        WHERE b.resourceId = :resourceId
          AND b.startTime < :end
          AND b.endTime > :start
    """)
    List<BookingJpaEntity> findOverlappingBookings(
            @Param("resourceId") UUID resourceId,
            @Param("start") Instant start,
            @Param("end") Instant end
    );
}