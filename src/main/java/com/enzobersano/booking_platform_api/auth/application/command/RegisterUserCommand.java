package com.enzobersano.booking_platform_api.auth.application.command;

public record RegisterUserCommand(String email, String rawPassword, String role) {}