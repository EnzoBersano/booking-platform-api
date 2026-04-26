package com.enzobersano.booking_platform_api.resource.api.mapper;

import com.enzobersano.booking_platform_api.shared.api.dto.ErrorResponse;
import com.enzobersano.booking_platform_api.resource.domain.failure.ResourceFailure;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResourceErrorMapper {

    public ResponseEntity<ErrorResponse> toResponse(ResourceFailure error) {

        if (error instanceof ResourceFailure.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.message()));
        }

        if (error instanceof ResourceFailure.InvalidType e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.message()));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Unexpected error"));
    }
}