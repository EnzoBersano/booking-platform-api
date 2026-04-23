package com.enzobersano.booking_platform_api.auth.application;

import com.enzobersano.booking_platform_api.auth.application.command.RegisterUserCommand;
import com.enzobersano.booking_platform_api.auth.application.port.UserRepositoryPort;
import com.enzobersano.booking_platform_api.auth.domain.AuthFailure;
import com.enzobersano.booking_platform_api.auth.domain.model.Role;
import com.enzobersano.booking_platform_api.auth.domain.model.User;
import com.enzobersano.booking_platform_api.auth.domain.service.PasswordPolicyService;
import com.enzobersano.booking_platform_api.auth.domain.valueobject.Email;
import com.enzobersano.booking_platform_api.auth.domain.valueobject.HashedPassword;
import com.enzobersano.booking_platform_api.shared.result.Result;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterUserUseCase {

    private final UserRepositoryPort   userRepository;
    private final PasswordEncoder      passwordEncoder;
    private final PasswordPolicyService passwordPolicy = new PasswordPolicyService();

    public RegisterUserUseCase(UserRepositoryPort userRepository,
                               PasswordEncoder passwordEncoder) {
        this.userRepository  = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Result<User, AuthFailure> execute(RegisterUserCommand command) {

        // 1. Email
        var emailResult = Email.of(command.email());
        if (!emailResult.isSuccess()) {
            return Result.failure(emailResult.error());
        }
        var email = emailResult.value();

        // 2. Duplicate
        if (userRepository.existsByEmail(email)) {
            return Result.failure(
                    new AuthFailure.UserAlreadyExists()
            );
        }

        // 3. Password policy
        var policyResult = passwordPolicy.validate(command.rawPassword());
        if (!policyResult.isSuccess()) {
            return Result.failure(policyResult.error());
        }

        // 4. Role
        var roleResult = resolveRole(command.role());
        if (!roleResult.isSuccess()) {
            return Result.failure(roleResult.error());
        }

        // 5. Hash
        var hashed = new HashedPassword(passwordEncoder.encode(command.rawPassword()));

        // 6. Persist
        var user  = User.create(email, hashed, roleResult.value());
        var saved = userRepository.save(user);

        return Result.success(saved);
    }
    private Result<Role, AuthFailure> resolveRole(String raw) {
        if (raw == null || raw.isBlank()) {
            return Result.success(Role.USER);
        }

        return switch (raw.toUpperCase().trim()) {
            case "USER"  -> Result.success(Role.USER);
            case "ADMIN" -> Result.success(Role.ADMIN);
            default      -> Result.failure(
                    new AuthFailure.InvalidRole("Unknown role: " + raw)
            );
        };
    }
}