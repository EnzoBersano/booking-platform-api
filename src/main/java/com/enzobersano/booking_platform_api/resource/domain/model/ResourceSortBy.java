package com.enzobersano.booking_platform_api.resource.domain.model;

import com.enzobersano.booking_platform_api.resource.domain.failure.ResourceFailure;
import com.enzobersano.booking_platform_api.shared.result.Result;

public enum ResourceSortBy {

    NAME("name"),
    TYPE("type"),
    STATUS("status");

    private final String field;

    ResourceSortBy(String field) {
        this.field = field;
    }

    public String field() {
        return field;
    }

    public static Result<ResourceSortBy, ResourceFailure> from(String value) {
        return switch (value.toLowerCase()) {
            case "name" -> Result.success(NAME);
            case "type" -> Result.success(TYPE);
            case "status" -> Result.success(STATUS);
            default -> Result.failure(
                    new ResourceFailure.InvalidSortBy(value)
            );
        };
    }
}