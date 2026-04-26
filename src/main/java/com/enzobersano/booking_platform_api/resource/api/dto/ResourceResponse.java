package com.enzobersano.booking_platform_api.resource.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Resource representation")
public record ResourceResponse(

        UUID id,
        String name,
        String type,
        String status
) {}