package com.enzobersano.booking_platform_api.auth.infrastructure.persistence;

import com.enzobersano.booking_platform_api.auth.domain.model.Role;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "users",
        indexes = @Index(name = "idx_users_email", columnList = "email", unique = true)
)
public class UserJpaEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, unique = true, length = 320)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Getter
    @Column(nullable = false)
    private boolean active;

    protected UserJpaEntity() {}

    public UserJpaEntity(UUID id, String email, String passwordHash,
                         Role role, Instant createdAt, boolean active) {
        this.id           = id;
        this.email        = email;
        this.passwordHash = passwordHash;
        this.role         = role;
        this.createdAt    = createdAt;
        this.active       = active;
    }


    public UUID    id()           { return id; }
    public String  email()        { return email; }
    public String  passwordHash() { return passwordHash; }
    public Role    role()         { return role; }
    public Instant createdAt()    { return createdAt; }
}