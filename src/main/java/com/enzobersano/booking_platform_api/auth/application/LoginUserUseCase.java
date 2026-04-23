package com.enzobersano.booking_platform_api.auth.application;

import com.enzobersano.booking_platform_api.auth.application.command.LoginCommand;
import com.enzobersano.booking_platform_api.auth.application.port.TokenPort;
import com.enzobersano.booking_platform_api.auth.application.port.UserRepositoryPort;
import com.enzobersano.booking_platform_api.shared.result.AccountDisabled;
import com.enzobersano.booking_platform_api.shared.result.InvalidCredentials;
import com.enzobersano.booking_platform_api.auth.domain.valueobject.Email;
import com.enzobersano.booking_platform_api.shared.result.Result;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginUserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder    passwordEncoder;
    private final TokenPort          tokenPort;

    public LoginUserUseCase(UserRepositoryPort userRepository,
                            PasswordEncoder passwordEncoder,
                            TokenPort tokenPort) {
        this.userRepository  = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenPort       = tokenPort;
    }

    @Transactional(readOnly = true)
    public Result<String> execute(LoginCommand command) {

        // 1. Parse email — return generic error to avoid user enumeration
        var emailResult = Email.of(command.email());
        if (!emailResult.isSuccess()) {
            return Result.failure(new InvalidCredentials());
        }

        // 2. Look up user
        var maybeUser = userRepository.findByEmail(emailResult.getValue());
        if (maybeUser.isEmpty()) {
            return Result.failure(new InvalidCredentials());
        }

        var user = maybeUser.get();

        // 3. Check account status before password verification (fail fast)
        if (!user.isActive()) {
            return Result.failure(new AccountDisabled());
        }

        // 4. Constant-time BCrypt comparison
        if (!passwordEncoder.matches(command.rawPassword(), user.password().value())) {
            return Result.failure(new InvalidCredentials());
        }

        // 5. Issue token
        return Result.success(tokenPort.generateToken(user));
    }
}