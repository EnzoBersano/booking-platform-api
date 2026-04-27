package com.enzobersano.booking_platform_api.booking.domain.model;

import com.enzobersano.booking_platform_api.booking.domain.valueobject.TimeRange;

import java.util.UUID;

public class Booking {

    private final UUID id;
    private final UUID resourceId;
    private final UUID userId;
    private final TimeRange timeRange;
    private BookingStatus status;

    private Booking(
            UUID id,
            UUID resourceId,
            UUID userId,
            TimeRange timeRange,
            BookingStatus status
    ) {
        this.id = id;
        this.resourceId = resourceId;
        this.userId = userId;
        this.timeRange = timeRange;
        this.status = status;
    }

    public static Booking create(
            UUID resourceId,
            UUID userId,
            TimeRange timeRange
    ) {
        return new Booking(
                UUID.randomUUID(),
                resourceId,
                userId,
                timeRange,
                BookingStatus.ACTIVE
        );
    }

    public void cancel() {
        this.status = BookingStatus.CANCELLED;
    }

    public boolean overlaps(Booking other) {
        return this.resourceId.equals(other.resourceId)
                && this.timeRange.overlaps(other.timeRange);
    }

    public UUID id() { return id; }
    public UUID resourceId() { return resourceId; }
    public UUID userId() { return userId; }
    public TimeRange timeRange() { return timeRange; }
    public BookingStatus status() { return status; }
}