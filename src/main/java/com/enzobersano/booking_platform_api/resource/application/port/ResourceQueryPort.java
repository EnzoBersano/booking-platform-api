package com.enzobersano.booking_platform_api.resource.application.port;

import java.util.UUID;

public interface ResourceQueryPort {
    boolean exists(UUID resourceId);
    boolean isActive(UUID resourceId);
}
