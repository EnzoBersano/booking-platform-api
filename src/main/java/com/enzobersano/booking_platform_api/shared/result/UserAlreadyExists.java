package com.enzobersano.booking_platform_api.shared.result;

public record UserAlreadyExists() implements AuthError {
    @Override
    public String message() { return "User already exists"; }
}
