package com.enzobersano.booking_platform_api.auth.api.mapper;

import com.enzobersano.booking_platform_api.auth.api.dto.ErrorResponse;
import com.enzobersano.booking_platform_api.auth.domain.AuthFailure;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class AuthErrorMapper {

    public ResponseEntity<ErrorResponse> toResponse(AuthFailure error) {

        if (error instanceof AuthFailure.UserAlreadyExists e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(e.message()));
        }

        if (error instanceof AuthFailure.InvalidCredentials e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(e.message()));
        }

        if (error instanceof AuthFailure.AccountDisabled e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse(e.message()));
        }

        if (error instanceof AuthFailure.WeakPassword e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.message()));
        }

        if (error instanceof AuthFailure.InvalidEmailFormat e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.message()));
        }

        if (error instanceof AuthFailure.InvalidRole e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.message()));
        }

        if (error instanceof AuthFailure.UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.message()));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Unexpected error"));
    }
}