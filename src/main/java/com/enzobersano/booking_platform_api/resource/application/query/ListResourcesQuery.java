package com.enzobersano.booking_platform_api.resource.application.query;

import com.enzobersano.booking_platform_api.resource.domain.model.ResourceSortBy;
import com.enzobersano.booking_platform_api.resource.domain.model.ResourceSortDirection;

public record ListResourcesQuery(
        int page,
        int size,
        ResourceSortBy sortBy,
        ResourceSortDirection direction
) {}