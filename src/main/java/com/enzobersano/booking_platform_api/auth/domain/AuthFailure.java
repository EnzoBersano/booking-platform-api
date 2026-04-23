package com.enzobersano.booking_platform_api.auth.domain;

import com.enzobersano.booking_platform_api.shared.result.Failure;

/**
 * Auth-specific failure subtypes.
 * Controllers map these to HTTP status codes — nothing else needs to know about HTTP here.
 */
public sealed interface AuthFailure extends Failure
        permits AuthFailure.AccountDisabled,
        AuthFailure.InvalidCredentials,
        AuthFailure.InvalidEmailFormat,
        AuthFailure.InvalidRole,
        AuthFailure.UserAlreadyExists,
        AuthFailure.UserNotFound,
        AuthFailure.WeakPassword {

    record InvalidCredentials() implements AuthFailure {
        public String message() { return "Invalid email or password"; }
    }

    record UserNotFound() implements AuthFailure {
        public String message() { return "User not found"; }
    }

    record WeakPassword(String message) implements AuthFailure {}

    record InvalidEmailFormat() implements AuthFailure {
        public String message() { return "Invalid email format"; }
    }

    record AccountDisabled() implements AuthFailure {
        public String message() { return "Account is disabled"; }
    }

    record UserAlreadyExists() implements AuthFailure {
        public String message() { return "User already exists"; }
    }

    record InvalidRole(String message) implements AuthFailure {}
}