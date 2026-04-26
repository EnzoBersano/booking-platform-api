package com.enzobersano.booking_platform_api.resource.domain.model;

import com.enzobersano.booking_platform_api.resource.domain.failure.ResourceFailure;
import com.enzobersano.booking_platform_api.shared.result.Result;

public enum ResourceSortDirection {
    ASC,
    DESC;

    public static Result<ResourceSortDirection, ResourceFailure> from(String value) {
        return switch (value.toLowerCase()) {
            case "asc" -> Result.success(ASC);
            case "desc" -> Result.success(DESC);
            default -> Result.failure(
                    new ResourceFailure.InvalidSortDirection(value)
            );
        };
    }
}