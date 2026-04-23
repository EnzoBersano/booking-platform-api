package com.enzobersano.booking_platform_api.shared.result;

/**
 * Root sealed error type.
 * Every module adds its own permitted subtype here.
 */
public sealed interface Error
        permits AuthError, ValidationError {

    String message();
}