package com.enzobersano.booking_platform_api.booking.infrastructure.persistence;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "bookings")
public class BookingJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID resourceId;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private Instant startTime;

    @Column(nullable = false)
    private Instant endTime;

    @Column(nullable = false)
    private String status;

    protected BookingJpaEntity() {}

    public BookingJpaEntity(
            UUID id,
            UUID resourceId,
            UUID userId,
            Instant startTime,
            Instant endTime,
            String status
    ) {
        this.id = id;
        this.resourceId = resourceId;
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public UUID getId() { return id; }
    public UUID getResourceId() { return resourceId; }
    public UUID getUserId() { return userId; }
    public Instant getStartTime() { return startTime; }
    public Instant getEndTime() { return endTime; }
    public String getStatus() { return status; }
}