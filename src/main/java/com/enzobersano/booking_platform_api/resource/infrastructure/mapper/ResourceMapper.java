package com.enzobersano.booking_platform_api.resource.infrastructure.mapper;

import com.enzobersano.booking_platform_api.resource.domain.model.Resource;
import com.enzobersano.booking_platform_api.resource.infrastructure.persistence.ResourceJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ResourceMapper {

    public ResourceJpaEntity toEntity(Resource r) {
        return new ResourceJpaEntity(
                r.id(),
                r.name(),
                r.type(),
                r.status()
        );
    }

    public Resource toDomain(ResourceJpaEntity e) {
        return Resource.reconstitute(
                e.id(),
                e.name(),
                e.type(),
                e.status()
        );
    }
}