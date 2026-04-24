package com.enzobersano.booking_platform_api.auth;

import com.enzobersano.booking_platform_api.auth.api.AuthController;
import com.enzobersano.booking_platform_api.auth.api.mapper.AuthErrorMapper;
import com.enzobersano.booking_platform_api.auth.application.LoginUserUseCase;
import com.enzobersano.booking_platform_api.auth.application.RegisterUserUseCase;
import com.enzobersano.booking_platform_api.auth.application.port.TokenPort;
import com.enzobersano.booking_platform_api.auth.domain.AuthFailure;
import com.enzobersano.booking_platform_api.auth.domain.model.Role;
import com.enzobersano.booking_platform_api.auth.domain.model.User;
import com.enzobersano.booking_platform_api.auth.domain.valueobject.Email;
import com.enzobersano.booking_platform_api.auth.domain.valueobject.HashedPassword;
import com.enzobersano.booking_platform_api.auth.infrastructure.security.JwtAuthenticationFilter;
import com.enzobersano.booking_platform_api.auth.infrastructure.security.SecurityConfig;
import com.enzobersano.booking_platform_api.shared.result.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.security.test.context.support.WithMockUser;
import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ImportAutoConfiguration(exclude = SecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(AuthErrorMapper.class)
@WithMockUser
class AuthControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean RegisterUserUseCase registerUseCase;
    @MockBean LoginUserUseCase    loginUseCase;

    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    TokenPort  tokenPort;


    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private static final String REGISTER_URL = "/api/auth/register";
    private static final String LOGIN_URL    = "/api/auth/login";
    private static final String VALID_EMAIL  = "enzo@example.com";
    private static final String VALID_PASS   = "Secure1!";
    private static final String JWT          = "mocked.jwt.token";

    private String json(Object o) throws Exception {
        return objectMapper.writeValueAsString(o);
    }

    record RegisterBody(String email, String password, String role) {}
    record LoginBody(String email, String password) {}

    private User fakeUser() {
        var email    = Email.of(VALID_EMAIL).value();
        var password = new HashedPassword("hashed");
        return User.reconstitute(UUID.randomUUID(), email, password, Role.USER, Instant.now(), true);
    }

    // =========================================================================
    // POST /api/auth/register
    // =========================================================================

    @Nested
    @DisplayName("POST /api/auth/register")
    class Register {

        @Test
        @DisplayName("201 — valid payload registers and returns token")
        void returns201OnSuccess() throws Exception {
            when(registerUseCase.execute(any())).thenReturn(Result.success(fakeUser()));
            when(loginUseCase.execute(any())).thenReturn(Result.success(JWT));

            mockMvc.perform(post(REGISTER_URL).with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json(new RegisterBody(VALID_EMAIL, VALID_PASS, "USER"))))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.token").value(JWT))
                    .andExpect(jsonPath("$.tokenType").value("Bearer"))
                    .andExpect(jsonPath("$.role").value("USER"));
        }

        @Test
        @DisplayName("400 — blank email fails Bean Validation")
        void returns400OnBlankEmail() throws Exception {
            mockMvc.perform(post(REGISTER_URL).with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json(new RegisterBody("", VALID_PASS, "USER"))))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("400 — password shorter than 8 chars fails Bean Validation")
        void returns400OnShortPassword() throws Exception {
            mockMvc.perform(post(REGISTER_URL).with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json(new RegisterBody(VALID_EMAIL, "Ab1!", "USER"))))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("400 — weak password rejected by domain policy")
        void returns400OnWeakPassword() throws Exception {
            when(registerUseCase.execute(any()))
                    .thenReturn(Result.failure(new AuthFailure.WeakPassword("Password must contain at least one digit")));

            mockMvc.perform(post(REGISTER_URL).with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json(new RegisterBody(VALID_EMAIL, VALID_PASS, "USER"))))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("Password must contain at least one digit"));
        }

        @Test
        @DisplayName("400 — unknown role rejected")
        void returns400OnInvalidRole() throws Exception {
            when(registerUseCase.execute(any()))
                    .thenReturn(Result.failure(new AuthFailure.InvalidRole("Unknown role: GOD")));

            mockMvc.perform(post(REGISTER_URL).with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json(new RegisterBody(VALID_EMAIL, VALID_PASS, "GOD"))))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("Unknown role: GOD"));
        }

        @Test
        @DisplayName("409 — duplicate email returns conflict")
        void returns409OnDuplicateEmail() throws Exception {
            when(registerUseCase.execute(any()))
                    .thenReturn(Result.failure(new AuthFailure.UserAlreadyExists()));

            mockMvc.perform(post(REGISTER_URL).with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json(new RegisterBody(VALID_EMAIL, VALID_PASS, "USER"))))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.message").value("User already exists"));
        }
    }

    // =========================================================================
    // POST /api/auth/login
    // =========================================================================

    @Nested
    @DisplayName("POST /api/auth/login")
    class Login {

        @Test
        @DisplayName("200 — valid credentials return token")
        void returns200OnSuccess() throws Exception {
            when(loginUseCase.execute(any())).thenReturn(Result.success(JWT));

            mockMvc.perform(post(LOGIN_URL).with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json(new LoginBody(VALID_EMAIL, VALID_PASS))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").value(JWT))
                    .andExpect(jsonPath("$.tokenType").value("Bearer"));
        }

        @Test
        @DisplayName("400 — blank email fails Bean Validation")
        void returns400OnBlankEmail() throws Exception {
            mockMvc.perform(post(LOGIN_URL).with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json(new LoginBody("", VALID_PASS))))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("400 — blank password fails Bean Validation")
        void returns400OnBlankPassword() throws Exception {
            mockMvc.perform(post(LOGIN_URL).with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json(new LoginBody(VALID_EMAIL, ""))))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("401 — wrong password returns unauthorized")
        void returns401OnBadCredentials() throws Exception {
            when(loginUseCase.execute(any()))
                    .thenReturn(Result.failure(new AuthFailure.InvalidCredentials()));

            mockMvc.perform(post(LOGIN_URL).with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json(new LoginBody(VALID_EMAIL, "wrongpass"))))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").value("Invalid email or password"));
        }

        @Test
        @DisplayName("403 — disabled account returns forbidden")
        void returns403OnDisabledAccount() throws Exception {
            when(loginUseCase.execute(any()))
                    .thenReturn(Result.failure(new AuthFailure.AccountDisabled()));

            mockMvc.perform(post(LOGIN_URL).with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json(new LoginBody(VALID_EMAIL, VALID_PASS))))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.message").value("Account is disabled"));
        }
    }
}