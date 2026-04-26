package com.enzobersano.booking_platform_api.resource.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record ListResourcesRequest(

        @Schema(description = "Page number (0-based)", example = "0", defaultValue = "0")
        @Min(0)
        Integer page,

        @Schema(description = "Page size", example = "10", defaultValue = "10")
        @Min(1)
        @Max(100)
        Integer size,

        @Schema(
                description = "Sort field",
                allowableValues = {"name", "type", "status"},
                defaultValue = "name"
        )
        String sortBy,

        @Schema(
                description = "Sort direction",
                allowableValues = {"asc", "desc"},
                defaultValue = "asc"
        )
        String direction
) {

    public int pageOrDefault() {
        return page == null ? 0 : page;
    }

    public int sizeOrDefault() {
        return size == null ? 10 : size;
    }

    public String sortByOrDefault() {
        return sortBy == null || sortBy.isBlank()
                ? "name"
                : sortBy;
    }

    public String directionOrDefault() {
        return direction == null || direction.isBlank()
                ? "asc"
                : direction;
    }
}