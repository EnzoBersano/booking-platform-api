package com.enzobersano.booking_platform_api.resource.domain.model;

import com.enzobersano.booking_platform_api.resource.domain.failure.ResourceFailure;
import com.enzobersano.booking_platform_api.shared.result.Result;

public enum ResourceType {
    SERVICE,
    ROOM,
    FLIGHT,
    SUBSCRIPTION;

    public static Result<ResourceType, ResourceFailure> from(String input) {

        if (input == null || input.isBlank()) {
            return Result.failure(
                    new ResourceFailure.InvalidType("null or blank")
            );
        }

        try {
            return Result.success(
                    ResourceType.valueOf(input.trim().toUpperCase())
            );
        } catch (IllegalArgumentException e) {
            return Result.failure(
                    new ResourceFailure.InvalidType(input)
            );
        }
    }
}
