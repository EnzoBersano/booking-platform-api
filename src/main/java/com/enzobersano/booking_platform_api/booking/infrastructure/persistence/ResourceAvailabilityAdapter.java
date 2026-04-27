package com.enzobersano.booking_platform_api.booking.infrastructure.persistence;

import com.enzobersano.booking_platform_api.booking.application.port.ResourceAvailabilityPort;
import com.enzobersano.booking_platform_api.resource.application.port.ResourceQueryPort;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class ResourceAvailabilityAdapter implements ResourceAvailabilityPort {

    private final ResourceQueryPort resourceQueryPort;

    public ResourceAvailabilityAdapter(ResourceQueryPort resourceQueryPort) {
        this.resourceQueryPort = resourceQueryPort;
    }

    @Override
    public boolean exists(UUID resourceId) {
        return resourceQueryPort.exists(resourceId);
    }

    @Override
    public boolean isActive(UUID resourceId) {
        return resourceQueryPort.isActive(resourceId);
    }
}