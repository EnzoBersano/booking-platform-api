package com.enzobersano.booking_platform_api.shared.result;

public sealed interface AuthError extends Error
        permits InvalidCredentials,
        UserAlreadyExists,
        AccountDisabled,
        WeakPassword {
}