package com.enzobersano.booking_platform_api.booking.api.mapper;

import com.enzobersano.booking_platform_api.booking.domain.failure.BookingFailure;
import com.enzobersano.booking_platform_api.shared.api.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class BookingErrorMapper {

    public ResponseEntity<ErrorResponse> toResponse(
            BookingFailure error
    ) {

        if (error instanceof BookingFailure.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.message()));
        }

        if (error instanceof BookingFailure.InvalidTimeRange e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.message()));
        }

        if (error instanceof BookingFailure.InvalidSortBy e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.message()));
        }

        if (error instanceof BookingFailure.InvalidSortDirection e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.message()));
        }

        if (error instanceof BookingFailure.Conflict e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(e.message()));
        }

        return ResponseEntity.internalServerError()
                .body(new ErrorResponse("Unexpected error"));
    }
}