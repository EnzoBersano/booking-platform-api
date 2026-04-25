package com.enzobersano.booking_platform_api.resource.infrastructure.persistence;

import com.enzobersano.booking_platform_api.resource.domain.model.ResourceStatus;
import com.enzobersano.booking_platform_api.resource.domain.model.ResourceType;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "resources")
public class ResourceJpaEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResourceType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResourceStatus status;

    protected ResourceJpaEntity() {}

    public ResourceJpaEntity(UUID id, String name, ResourceType type, ResourceStatus status) {
        this.id     = id;
        this.name   = name;
        this.type   = type;
        this.status = status;
    }

    public UUID id() { return id; }
    public String name() { return name; }
    public ResourceType type() { return type; }
    public ResourceStatus status() { return status; }
}