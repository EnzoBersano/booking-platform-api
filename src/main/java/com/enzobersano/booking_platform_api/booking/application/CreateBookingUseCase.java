package com.enzobersano.booking_platform_api.booking.application;

import com.enzobersano.booking_platform_api.auth.application.port.CurrentUserPort;
import com.enzobersano.booking_platform_api.booking.application.command.CreateBookingCommand;
import com.enzobersano.booking_platform_api.booking.application.port.BookingRepositoryPort;
import com.enzobersano.booking_platform_api.booking.application.port.ResourceAvailabilityPort;
import com.enzobersano.booking_platform_api.booking.domain.failure.BookingFailure;
import com.enzobersano.booking_platform_api.booking.domain.model.Booking;
import com.enzobersano.booking_platform_api.booking.domain.policy.BookingPolicy;
import com.enzobersano.booking_platform_api.booking.domain.valueobject.TimeRange;
import com.enzobersano.booking_platform_api.shared.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class CreateBookingUseCase {

    private final BookingRepositoryPort repository;
    private final ResourceAvailabilityPort resourcePort;
    private final BookingPolicy bookingPolicy;
    private final CurrentUserPort currentUserPort;

    public CreateBookingUseCase(
            BookingRepositoryPort repository,
            ResourceAvailabilityPort resourcePort,
            BookingPolicy bookingPolicy, CurrentUserPort currentUserPort
    ) {
        this.repository = repository;
        this.resourcePort = resourcePort;
        this.bookingPolicy = bookingPolicy;
        this.currentUserPort = currentUserPort;
    }

    @Transactional
    public Result<Booking, BookingFailure> execute(
            CreateBookingCommand command
    ) {

        if (!resourcePort.exists(command.resourceId())) {
            return Result.failure(new BookingFailure.NotFound());
        }

        TimeRange timeRange;
        try {
            timeRange = new TimeRange(
                    command.startTime(),
                    command.endTime()
            );
        } catch (IllegalArgumentException ex) {
            return Result.failure(
                    new BookingFailure.InvalidTimeRange(ex.getMessage())
            );
        }

        var userId = currentUserPort.getCurrentUserId();
        var booking = Booking.create(
                command.resourceId(),
                userId,
                timeRange
        );

        var existingBookings =
                repository.findByResourceIdAndRange(
                        command.resourceId(),
                        command.startTime(),
                        command.endTime()
                );

        var validation = bookingPolicy.validate(
                booking,
                existingBookings,
                resourcePort.isActive(command.resourceId()),
                Instant.now()
        );

        if (!validation.isSuccess()) {
            return Result.failure(validation.error());
        }

        var saved = repository.save(booking);

        return Result.success(saved);
    }
}