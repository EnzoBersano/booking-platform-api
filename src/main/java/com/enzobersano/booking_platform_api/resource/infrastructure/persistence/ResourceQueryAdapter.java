package com.enzobersano.booking_platform_api.resource.infrastructure.persistence;

import com.enzobersano.booking_platform_api.resource.application.port.ResourceQueryPort;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class ResourceQueryAdapter implements ResourceQueryPort {

    private final ResourceJpaRepository repository;

    public ResourceQueryAdapter(ResourceJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean exists(UUID resourceId) {
        return repository.existsById(resourceId);
    }

    @Override
    public boolean isActive(UUID resourceId) {
        return repository.findById(resourceId)
                .map(resource -> resource.status().isActive())
                .orElse(false);
    }
}