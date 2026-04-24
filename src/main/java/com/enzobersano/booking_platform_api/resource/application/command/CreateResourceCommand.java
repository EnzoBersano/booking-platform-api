package com.enzobersano.booking_platform_api.resource.application.command;

public record CreateResourceCommand(
        String name,
        String type
) {}
