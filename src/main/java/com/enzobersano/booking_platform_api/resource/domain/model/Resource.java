package com.enzobersano.booking_platform_api.resource.domain.model;

import java.util.Objects;
import java.util.UUID;

public final class Resource {

    private final UUID id;
    private final String name;
    private final ResourceType type;
    private ResourceStatus status;

    private Resource(UUID id, String name, ResourceType type, ResourceStatus status) {
        this.id = Objects.requireNonNull(id);
        this.name = validateName(name);
        this.type = Objects.requireNonNull(type);
        this.status = Objects.requireNonNull(status);
    }

    public static Resource create(String name, ResourceType type) {
        return new Resource(
                UUID.randomUUID(),
                name,
                type,
                ResourceStatus.ACTIVE
        );
    }

    public static Resource reconstitute(UUID id, String name, ResourceType type, ResourceStatus status) {
        return new Resource(id, name, type, status);
    }

    public void deactivate() {
        if (this.status == ResourceStatus.INACTIVE) return;
        this.status = ResourceStatus.INACTIVE;
    }

    private String validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Resource name must not be blank");
        }
        return name.trim();
    }

    public UUID id() { return id; }
    public String name() { return name; }
    public ResourceType type() { return type; }
    public ResourceStatus status() { return status; }

    public boolean isActive() {
        return status == ResourceStatus.ACTIVE;
    }
}
