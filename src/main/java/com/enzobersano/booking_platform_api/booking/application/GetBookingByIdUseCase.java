package com.enzobersano.booking_platform_api.booking.application;

import com.enzobersano.booking_platform_api.booking.application.port.BookingRepositoryPort;
import com.enzobersano.booking_platform_api.booking.domain.failure.BookingFailure;
import com.enzobersano.booking_platform_api.booking.domain.model.Booking;
import com.enzobersano.booking_platform_api.shared.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class GetBookingByIdUseCase {

    private final BookingRepositoryPort repository;

    public GetBookingByIdUseCase(
            BookingRepositoryPort repository
    ) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Result<Booking, BookingFailure> execute(
            UUID bookingId
    ) {

        var bookingOpt = repository.findById(bookingId);

        if (bookingOpt.isEmpty()) {
            return Result.failure(
                    new BookingFailure.NotFound()
            );
        }

        return Result.success(bookingOpt.get());
    }
}