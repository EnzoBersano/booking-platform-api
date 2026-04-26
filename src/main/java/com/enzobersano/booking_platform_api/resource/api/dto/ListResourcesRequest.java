package com.enzobersano.booking_platform_api.resource.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record ListResourcesRequest(

        @Min(0)
        Integer page,

        @Min(1)
        @Max(100)
        Integer size
) {

    public int pageOrDefault() {
        return page == null ? 0 : page;
    }

    public int sizeOrDefault() {
        return size == null ? 10 : size;
    }
}