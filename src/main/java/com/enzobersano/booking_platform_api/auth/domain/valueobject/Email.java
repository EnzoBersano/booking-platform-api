package com.enzobersano.booking_platform_api.auth.domain.valueobject;

import com.enzobersano.booking_platform_api.shared.result.Result;
import com.enzobersano.booking_platform_api.shared.result.ValidationError;

import java.util.Objects;
import java.util.regex.Pattern;

public final class Email {

    private static final Pattern PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private final String value;

    private Email(String value) {
        this.value = value;
    }

    public static Result<Email> of(String raw) {
        if (raw == null || raw.isBlank()) {
            return Result.failure(new ValidationError("Email must not be blank"));
        }
        var normalized = raw.toLowerCase().trim();
        if (!PATTERN.matcher(normalized).matches()) {
            return Result.failure(new ValidationError("Invalid email format: " + raw));
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