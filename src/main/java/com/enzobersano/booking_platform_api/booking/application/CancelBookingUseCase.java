package com.enzobersano.booking_platform_api.booking.application;

import com.enzobersano.booking_platform_api.booking.application.command.CancelBookingCommand;
import com.enzobersano.booking_platform_api.booking.application.port.BookingRepositoryPort;
import com.enzobersano.booking_platform_api.booking.domain.failure.BookingFailure;
import com.enzobersano.booking_platform_api.shared.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CancelBookingUseCase {

    private final BookingRepositoryPort repository;

    public CancelBookingUseCase(
            BookingRepositoryPort repository
    ) {
        this.repository = repository;
    }

    @Transactional
    public Result<Void, BookingFailure> execute(
            CancelBookingCommand command
    ) {

        var bookingOpt =
                repository.findById(command.bookingId());

        if (bookingOpt.isEmpty()) {
            return Result.failure(
                    new BookingFailure.NotFound()
            );
        }

        var booking = bookingOpt.get();

        booking.cancel();

        repository.save(booking);

        return Result.success(null);
    }
}