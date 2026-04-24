package com.enzobersano.booking_platform_api.auth.application;

import com.enzobersano.booking_platform_api.auth.application.command.RegisterUserCommand;
import com.enzobersano.booking_platform_api.auth.application.port.UserRepositoryPort;
import com.enzobersano.booking_platform_api.auth.domain.AuthFailure;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @Mock UserRepositoryPort userRepository;
    @Mock PasswordEncoder passwordEncoder;

    @InjectMocks RegisterUserUseCase useCase;

    private final String email = "enzo@example.com";
    private final String password = "Password1!";


    @Test
    void returnsSuccess_whenDataIsValid() {

        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("hashed");
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        var result = useCase.execute(new RegisterUserCommand(email, password, "USER"));

        assertTrue(result.isSuccess());

        verify(userRepository).existsByEmail(any());
        verify(passwordEncoder).encode(any());
        verify(userRepository).save(any());
    }


    @Test
    void returnsFailure_whenEmailIsInvalid() {

        var result = useCase.execute(new RegisterUserCommand("", password, "USER"));

        assertFalse(result.isSuccess());
        assertInstanceOf(AuthFailure.InvalidEmailFormat.class, result.error());

        verifyNoInteractions(userRepository, passwordEncoder);
    }


    @Test
    void returnsFailure_whenUserAlreadyExists() {

        when(userRepository.existsByEmail(any())).thenReturn(true);

        var result = useCase.execute(new RegisterUserCommand(email, password, "USER"));

        assertFalse(result.isSuccess());
        assertInstanceOf(AuthFailure.UserAlreadyExists.class, result.error());

        verify(userRepository).existsByEmail(any());
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(passwordEncoder);
    }


    @Test
    void returnsFailure_whenPasswordIsWeak() {

        when(userRepository.existsByEmail(any())).thenReturn(false);

        var result = useCase.execute(new RegisterUserCommand(email, "123", "USER"));

        assertFalse(result.isSuccess());
        assertInstanceOf(AuthFailure.WeakPassword.class, result.error());

        verify(userRepository).existsByEmail(any());
        verifyNoInteractions(passwordEncoder);
        verify(userRepository, never()).save(any());
    }


    @Test
    void returnsFailure_whenRoleIsInvalid() {

        when(userRepository.existsByEmail(any())).thenReturn(false);

        var result = useCase.execute(new RegisterUserCommand(email, password, "GOD"));

        assertFalse(result.isSuccess());
        assertInstanceOf(AuthFailure.InvalidRole.class, result.error());

        verify(userRepository).existsByEmail(any());
        verifyNoInteractions(passwordEncoder);
        verify(userRepository, never()).save(any());
    }
}