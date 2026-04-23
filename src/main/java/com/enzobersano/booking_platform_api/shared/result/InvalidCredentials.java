package com.enzobersano.booking_platform_api.shared.result;

public record InvalidCredentials() implements AuthError {
    @Override
    public String message() { return "Invalid credentials"; }
}
