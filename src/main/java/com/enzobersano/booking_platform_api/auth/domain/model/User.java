package com.enzobersano.booking_platform_api.auth.domain.model;

import com.enzobersano.booking_platform_api.auth.domain.valueobject.Email;
import com.enzobersano.booking_platform_api.auth.domain.valueobject.HashedPassword;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class User {

    private final UUID           id;
    private final Email          email;
    private final HashedPassword password;
    private final Role           role;
    private final Instant        createdAt;
    private       boolean        active;

    private User(UUID id, Email email, HashedPassword password,
                 Role role, Instant createdAt, boolean active) {
        this.id        = Objects.requireNonNull(id);
        this.email     = Objects.requireNonNull(email);
        this.password  = Objects.requireNonNull(password);
        this.role      = Objects.requireNonNull(role);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.active    = active;
    }

    /** Called when creating a brand-new user. */
    public static User create(Email email, HashedPassword password, Role role) {
        return new User(UUID.randomUUID(), email, password, role, Instant.now(), true);
    }

    /** Called when rehydrating from persistence — bypasses business-rule defaults. */
    public static User reconstitute(UUID id, Email email, HashedPassword password,
                                    Role role, Instant createdAt, boolean active) {
        return new User(id, email, password, role, createdAt, active);
    }

    public void deactivate() { this.active = false; }

    public UUID           id()        { return id; }
    public Email          email()     { return email; }
    public HashedPassword password()  { return password; }
    public Role           role()      { return role; }
    public Instant        createdAt() { return createdAt; }
    public boolean        isActive()  { return active; }
}