package com.enzobersano.booking_platform_api.resource.application.query;

public record ListResourcesQuery(
        int page,
        int size,
        ResourceSortBy sortBy,
        ResourceSortDirection direction
) {}