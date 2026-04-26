package com.enzobersano.booking_platform_api.resource.infrastructure.mapper;

import com.enzobersano.booking_platform_api.resource.api.dto.ResourceResponse;
import com.enzobersano.booking_platform_api.resource.domain.model.Resource;
import com.enzobersano.booking_platform_api.resource.infrastructure.persistence.ResourceJpaEntity;
import com.enzobersano.booking_platform_api.shared.pagination.PageResult;
import org.springframework.data.domain.Page;
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

    public PageResult<Resource> toPageResult(Page<ResourceJpaEntity> page) {

        var content = page.getContent()
                .stream()
                .map(this::toDomain)
                .toList();

        return new PageResult<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }
}