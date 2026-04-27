package com.enzobersano.booking_platform_api.booking.domain.valueobject;

import java.time.Instant;

public record TimeRange(
        Instant start,
        Instant end
) {

    public TimeRange {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Time range cannot be null");
        }

        if (!start.isBefore(end)) {
            throw new IllegalArgumentException("Start must be before end");
        }
    }

    public boolean overlaps(TimeRange other) {
        return start.isBefore(other.end) && end.isAfter(other.start);
    }

    public boolean contains(Instant instant) {
        return (instant.equals(start) || instant.isAfter(start))
                && instant.isBefore(end);
    }
}