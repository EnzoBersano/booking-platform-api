package com.enzobersano.booking_platform_api.shared.result;

public record AccountDisabled() implements AuthError {
    @Override
    public String message() { return "Account is disabled"; }
}