package com.enzobersano.booking_platform_api.resource.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Payload to create a resource")
public record CreateResourceRequest(

        @Schema(example = "Room 101")
        @NotBlank
        String name,

        @Schema(example = "ROOM")
        @NotBlank
        String type
) {}