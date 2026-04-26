package com.enzobersano.booking_platform_api.resource.api.mapper;

import com.enzobersano.booking_platform_api.resource.api.dto.ResourceResponse;
import com.enzobersano.booking_platform_api.resource.domain.model.Resource;
import com.enzobersano.booking_platform_api.shared.api.dto.PagedResponse;
import com.enzobersano.booking_platform_api.shared.pagination.PageResult;
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
    public PagedResponse<ResourceResponse> toPagedResponse(
            PageResult<Resource> page
    ) {
        return new PagedResponse<>(
                page.content().stream()
                        .map(this::toResponse)
                        .toList(),
                page.page(),
                page.size(),
                page.totalElements(),
                page.totalPages(),
                page.first(),
                page.last()
        );
    }
}