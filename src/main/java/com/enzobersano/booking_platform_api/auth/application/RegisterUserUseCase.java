package com.enzobersano.booking_platform_api.auth.application;

import com.enzobersano.booking_platform_api.auth.application.command.RegisterUserCommand;
import com.enzobersano.booking_platform_api.auth.application.port.UserRepositoryPort;
import com.enzobersano.booking_platform_api.shared.result.UserAlreadyExists;
import com.enzobersano.booking_platform_api.auth.domain.model.Role;
import com.enzobersano.booking_platform_api.auth.domain.model.User;
import com.enzobersano.booking_platform_api.auth.domain.service.PasswordPolicyService;
import com.enzobersano.booking_platform_api.auth.domain.valueobject.Email;
import com.enzobersano.booking_platform_api.auth.domain.valueobject.HashedPassword;
import com.enzobersano.booking_platform_api.shared.result.Result;
import com.enzobersano.booking_platform_api.shared.result.ValidationError;
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
    public Result<User> execute(RegisterUserCommand command) {

        // 1. Validate and construct Email value object
        var emailResult = Email.of(command.email());
        if (!emailResult.isSuccess()) {
            return Result.failure(emailResult.getError());
        }
        var email = emailResult.getValue();

        // 2. Guard duplicate
        if (userRepository.existsByEmail(email)) {
            return Result.failure(new UserAlreadyExists());
        }

        // 3. Enforce password policy (domain rule)
        var policyResult = passwordPolicy.validate(command.rawPassword());
        if (!policyResult.isSuccess()) {
            return Result.failure(policyResult.getError());
        }

        // 4. Resolve role — default USER
        var role = resolveRole(command.role());
        if (!role.isSuccess()) {
            return Result.failure(role.getError());
        }

        // 5. Hash password — infrastructure concern, happens at app layer boundary
        var hashed = new HashedPassword(passwordEncoder.encode(command.rawPassword()));

        // 6. Create aggregate and persist
        var user  = User.create(email, hashed, role.getValue());
        var saved = userRepository.save(user);

        return Result.success(saved);
    }

    private Result<Role> resolveRole(String raw) {
        if (raw == null || raw.isBlank()) return Result.success(Role.USER);
        return switch (raw.toUpperCase().trim()) {
            case "USER"  -> Result.success(Role.USER);
            case "ADMIN" -> Result.success(Role.ADMIN);
            default      -> Result.failure(new ValidationError("Unknown role: " + raw));
        };
    }
}