package com.enzobersano.booking_platform_api.resource.api.mapper;

import com.enzobersano.booking_platform_api.resource.api.dto.ResourceResponse;
import com.enzobersano.booking_platform_api.resource.domain.model.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResourceResponseMapper {

    public ResourceResponse toResponse(Resource resource) {
        return new ResourceResponse(
                resource.id(),
                resource.name(),
                resource.type().name(),
                resource.status().name()
        );
    }

    public List<ResourceResponse> toResponseList(List<Resource> resources) {
        return resources.stream()
                .map(this::toResponse)
                .toList();
    }
}