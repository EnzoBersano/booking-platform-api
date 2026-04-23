package com.enzobersano.booking_platform_api.auth.domain.valueobject;

import com.enzobersano.booking_platform_api.auth.domain.AuthFailure;
import com.enzobersano.booking_platform_api.shared.result.Result;
import java.util.Objects;
import java.util.regex.Pattern;

public final class Email {

    private static final Pattern PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private final String value;

    private Email(String value) {
        this.value = value;
    }

    public static Result<Email, AuthFailure> of(String raw) {
        if (raw == null || raw.isBlank()) {
            return Result.failure(
                    new AuthFailure.InvalidEmailFormat()
            );
        }

        var normalized = raw.toLowerCase().trim();

        if (!PATTERN.matcher(normalized).matches()) {
            return Result.failure(
                    new AuthFailure.InvalidEmailFormat()
            );
        }

        return Result.success(new Email(normalized));
    }

    public String value() { return value; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Email e)) return false;
        return Objects.equals(value, e.value);
    }

    @Override public int hashCode() { return Objects.hash(value); }
    @Override public String toString() { return value; }
}