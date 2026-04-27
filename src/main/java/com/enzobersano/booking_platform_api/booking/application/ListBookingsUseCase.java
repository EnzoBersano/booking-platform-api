package com.enzobersano.booking_platform_api.booking.application;

import com.enzobersano.booking_platform_api.booking.application.port.BookingRepositoryPort;
import com.enzobersano.booking_platform_api.booking.application.query.ListBookingsQuery;
import com.enzobersano.booking_platform_api.booking.domain.failure.BookingFailure;
import com.enzobersano.booking_platform_api.booking.domain.model.Booking;
import com.enzobersano.booking_platform_api.shared.pagination.PageResult;
import com.enzobersano.booking_platform_api.shared.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListBookingsUseCase {

    private final BookingRepositoryPort repository;

    public ListBookingsUseCase(
            BookingRepositoryPort repository
    ) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Result<PageResult<Booking>, BookingFailure> execute(
            ListBookingsQuery query
    ) {

        return Result.success(
                repository.findAll(query)
        );
    }
}