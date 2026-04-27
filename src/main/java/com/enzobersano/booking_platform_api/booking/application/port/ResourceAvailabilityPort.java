package com.enzobersano.booking_platform_api.booking.application.port;

import java.util.UUID;

public interface ResourceAvailabilityPort {

    boolean exists(UUID resourceId);

    boolean isActive(UUID resourceId);
}