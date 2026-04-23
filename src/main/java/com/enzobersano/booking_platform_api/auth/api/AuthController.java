package com.enzobersano.booking_platform_api.auth.api;

import com.enzobersano.booking_platform_api.auth.api.dto.AuthResponse;
import com.enzobersano.booking_platform_api.auth.api.dto.ErrorResponse;
import com.enzobersano.booking_platform_api.auth.api.dto.LoginRequest;
import com.enzobersano.booking_platform_api.auth.api.dto.RegisterRequest;
import com.enzobersano.booking_platform_api.auth.application.LoginUserUseCase;
import com.enzobersano.booking_platform_api.auth.application.RegisterUserUseCase;
import com.enzobersano.booking_platform_api.auth.application.command.LoginCommand;
import com.enzobersano.booking_platform_api.auth.application.command.RegisterUserCommand;
import com.enzobersano.booking_platform_api.shared.result.*;
import com.enzobersano.booking_platform_api.shared.result.Error;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "User registration and login")
public class AuthController {

    private final RegisterUserUseCase registerUseCase;
    private final LoginUserUseCase    loginUseCase;

    public AuthController(RegisterUserUseCase registerUseCase,
                          LoginUserUseCase loginUseCase) {
        this.registerUseCase = registerUseCase;
        this.loginUseCase    = loginUseCase;
    }

    // -------------------------------------------------------------------------
    // POST /api/v1/auth/register
    // -------------------------------------------------------------------------

    @Operation(summary = "Register a new user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registered successfully",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation or password policy failure",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email already registered",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        var command = new RegisterUserCommand(
                request.email(), request.password(), request.role()
        );
        var result = registerUseCase.execute(command);

        if (result.isSuccess()) {
            var user  = result.getValue();
            // Issue token immediately after registration
            var login = loginUseCase.execute(new LoginCommand(request.email(), request.password()));
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(AuthResponse.of(login.getValue(), user.role().name()));
        }

        return toErrorResponse(result.getError());
    }

    // -------------------------------------------------------------------------
    // POST /api/v1/auth/login
    // -------------------------------------------------------------------------

    @Operation(summary = "Authenticate and receive a JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Bad credentials",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Account disabled",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        var result = loginUseCase.execute(new LoginCommand(request.email(), request.password()));

        if (result.isSuccess()) {
            // Role is embedded in the token; we echo it in the response for convenience
            return ResponseEntity.ok(AuthResponse.of(result.getValue(), "USER"));
        }

        return toErrorResponse(result.getError());
    }


    private ResponseEntity<ErrorResponse> toErrorResponse(Error error) {

        if (error instanceof UserAlreadyExists e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(e.message()));
        }
        if (error instanceof InvalidCredentials e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(e.message()));
        }
        if (error instanceof AccountDisabled e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse(e.message()));
        }
        if (error instanceof WeakPassword e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.message()));
        }
        if (error instanceof ValidationError e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.message()));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Unexpected error"));
    }
}