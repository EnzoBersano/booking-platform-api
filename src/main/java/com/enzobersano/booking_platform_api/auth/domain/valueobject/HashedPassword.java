package com.enzobersano.booking_platform_api.auth.domain.valueobject;

import lombok.Getter;
import java.util.Objects;

/**
 * Represents an already-hashed password.
 * The domain never stores or handles raw plaintext passwords after hashing.
 */
@Getter
public final class HashedPassword {

    private final String value;

    public HashedPassword(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Hashed password must not be blank");
        }
        this.value = value;
    }

    public String value() { return value; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof HashedPassword h)) return false;
        return Objects.equals(value, h.value);
    }

    @Override public int hashCode() { return Objects.hash(value); }
}