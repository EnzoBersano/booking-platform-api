package com.enzobersano.booking_platform_api.booking.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record ListBookingsRequest(

        @Schema(example = "0", defaultValue = "0")
        @Min(0)
        Integer page,

        @Schema(example = "10", defaultValue = "10")
        @Min(1)
        @Max(100)
        Integer size,

        @Schema(
                allowableValues = {"startTime", "endTime", "status"},
                defaultValue = "startTime"
        )
        String sortBy,

        @Schema(
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
                ? "startTime"
                : sortBy;
    }

    public String directionOrDefault() {
        return direction == null || direction.isBlank()
                ? "asc"
                : direction;
    }
}