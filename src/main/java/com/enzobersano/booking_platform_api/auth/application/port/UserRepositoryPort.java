package com.enzobersano.booking_platform_api.auth.application.port;

import com.enzobersano.booking_platform_api.auth.domain.model.User;
import com.enzobersano.booking_platform_api.auth.domain.valueobject.Email;
import java.util.Optional;

/**
 * Port (interface) defined by the application layer.
 * The infrastructure adapter implements this — the domain/application never depends on JPA.
 */

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findByEmail(Email email);
    boolean existsByEmail(Email email);
}