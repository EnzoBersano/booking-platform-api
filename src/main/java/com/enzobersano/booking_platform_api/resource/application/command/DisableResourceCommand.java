package com.enzobersano.booking_platform_api.resource.application.command;

import java.util.UUID;

public record DisableResourceCommand(
        UUID resourceId
) {}
