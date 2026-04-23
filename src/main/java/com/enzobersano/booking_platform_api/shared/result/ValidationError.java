package com.enzobersano.booking_platform_api.shared.result;

public record ValidationError(String message) implements Error {}