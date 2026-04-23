package com.enzobersano.booking_platform_api.auth.infrastructure.persistence;

import com.enzobersano.booking_platform_api.auth.application.port.UserRepositoryPort;
import com.enzobersano.booking_platform_api.auth.domain.model.User;
import com.enzobersano.booking_platform_api.auth.domain.valueobject.Email;
import com.enzobersano.booking_platform_api.auth.domain.valueobject.HashedPassword;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository jpa;

    public UserRepositoryAdapter(UserJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public User save(User user) {
        return toDomain(jpa.save(toEntity(user)));
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return jpa.findByEmail(email.value()).map(this::toDomain);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpa.existsByEmail(email.value());
    }

    // -------------------------------------------------------------------------
    // Mapping — anti-corruption layer between JPA and domain
    // -------------------------------------------------------------------------

    private UserJpaEntity toEntity(User u) {
        return new UserJpaEntity(
                u.id(),
                u.email().value(),
                u.password().value(),
                u.role(),
                u.createdAt(),
                u.isActive()
        );
    }

    private User toDomain(UserJpaEntity e) {
        // Email.of() is safe here — data was validated on write path
        var email    = Email.of(e.email()).getValue();
        var password = new HashedPassword(e.passwordHash());
        return User.reconstitute(e.id(), email, password, e.role(), e.createdAt(), e.isActive());
    }
}