package com.enzobersano.booking_platform_api.auth.domain.service;

import com.enzobersano.booking_platform_api.auth.domain.AuthFailure;
import com.enzobersano.booking_platform_api.shared.result.Result;

/**
 * Pure domain service — no Spring, no framework.
 * Returns Result<Void>; null stands for "no meaningful success value".

 * Enforces password policy rules before hashing occurs.
 * Lives in the domain because these are business rules, not infrastructure concerns.
 *
 */
public final class PasswordPolicyService {

    private static final int MIN_LENGTH = 8;

    public Result<Void, AuthFailure> validate(String raw) {

        if (raw == null || raw.length() < MIN_LENGTH) {
            return Result.failure(
                    new AuthFailure.WeakPassword(
                            "Password must be at least %d characters".formatted(MIN_LENGTH)
                    )
            );
        }

        if (!raw.matches(".*[A-Z].*")) {
            return Result.failure(
                    new AuthFailure.WeakPassword(
                            "Password must contain at least one uppercase letter"
                    )
            );
        }

        if (!raw.matches(".*\\d.*")) {
            return Result.failure(
                    new AuthFailure.WeakPassword(
                            "Password must contain at least one digit"
                    )
            );
        }

        if (!raw.matches(".*[!@#$%^&*()].*")) {
            return Result.failure(
                    new AuthFailure.WeakPassword(
                            "Password must contain at least one special character"
                    )
            );
        }

        return Result.success(null);
    }
}