package com.enzobersano.booking_platform_api.auth.application;

import com.enzobersano.booking_platform_api.auth.application.command.LoginCommand;
import com.enzobersano.booking_platform_api.auth.application.port.TokenPort;
import com.enzobersano.booking_platform_api.auth.application.port.UserRepositoryPort;
import com.enzobersano.booking_platform_api.auth.domain.AuthFailure;
import com.enzobersano.booking_platform_api.auth.domain.model.User;
import com.enzobersano.booking_platform_api.auth.domain.valueobject.HashedPassword;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUserUseCaseTest {

    @Mock UserRepositoryPort userRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock TokenPort tokenPort;

    @InjectMocks LoginUserUseCase useCase;

    private final String email = "enzo@example.com";
    private final String password = "Password1!";
    private final User user = mock(User.class);


    @Test
    void returnsToken_whenCredentialsAreValid() {

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(user.isActive()).thenReturn(true);
        when(user.password()).thenReturn(new HashedPassword("hashed"));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(tokenPort.generateToken(user)).thenReturn("jwt-token");

        var result = useCase.execute(new LoginCommand(email, password));

        assertTrue(result.isSuccess());
        assertEquals("jwt-token", result.value());

        verify(tokenPort).generateToken(user);
    }


    @Test
    void returnsInvalidCredentials_whenEmailIsInvalid() {

        var result = useCase.execute(new LoginCommand("", password));

        assertFalse(result.isSuccess());
        assertInstanceOf(AuthFailure.InvalidCredentials.class, result.error());

        verifyNoInteractions(userRepository, passwordEncoder, tokenPort);
    }


    @Test
    void returnsInvalidCredentials_whenUserDoesNotExist() {

        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        var result = useCase.execute(new LoginCommand(email, password));

        assertFalse(result.isSuccess());
        assertInstanceOf(AuthFailure.InvalidCredentials.class, result.error());

        verify(userRepository).findByEmail(any());
        verifyNoInteractions(passwordEncoder, tokenPort);
    }


    @Test
    void returnsAccountDisabled_whenUserIsInactive() {

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(user.isActive()).thenReturn(false);

        var result = useCase.execute(new LoginCommand(email, password));

        assertFalse(result.isSuccess());
        assertInstanceOf(AuthFailure.AccountDisabled.class, result.error());

        verify(userRepository).findByEmail(any());
        verifyNoInteractions(tokenPort);
    }


    @Test
    void returnsInvalidCredentials_whenPasswordDoesNotMatch() {

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(user.isActive()).thenReturn(true);
        when(user.password()).thenReturn(new HashedPassword("hashed"));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        var result = useCase.execute(new LoginCommand(email, password));

        assertFalse(result.isSuccess());
        assertInstanceOf(AuthFailure.InvalidCredentials.class, result.error());

        verify(userRepository).findByEmail(any());
        verify(passwordEncoder).matches(any(), any());
        verifyNoInteractions(tokenPort);
    }
}