package com.enzobersano.booking_platform_api.auth.application.command;

public record LoginCommand(String email, String rawPassword) {}