package com.enzobersano.booking_platform_api.auth.domain;

import com.enzobersano.booking_platform_api.shared.result.Failure;

/**
 * Auth-specific failure subtypes.
 * Controllers map these to HTTP status codes — nothing else needs to know about HTTP here.
 */
public sealed interface AuthFailure implements Failure
        permits AuthFailure.EmailAlreadyExists,
        AuthFailure.InvalidCredentials,
        AuthFailure.UserNotFound,
        AuthFailure.WeakPassword,
        AuthFailure.InvalidEmailFormat,
        AuthFailure.AccountDisabled {

    record EmailAlreadyExists(String message) implements AuthFailure {}
    record InvalidCredentials(String message) implements AuthFailure {}
    record UserNotFound(String message)       implements AuthFailure {}
    record WeakPassword(String message)       implements AuthFailure {}
    record InvalidEmailFormat(String message) implements AuthFailure {}
    record AccountDisabled(String message)    implements AuthFailure {}
}