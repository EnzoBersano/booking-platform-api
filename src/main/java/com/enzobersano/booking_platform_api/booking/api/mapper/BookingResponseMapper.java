package com.enzobersano.booking_platform_api.booking.api.mapper;

import com.enzobersano.booking_platform_api.booking.api.dto.BookingResponse;
import com.enzobersano.booking_platform_api.booking.domain.model.Booking;
import com.enzobersano.booking_platform_api.shared.api.dto.PagedResponse;
import com.enzobersano.booking_platform_api.shared.pagination.PageResult;
import org.springframework.stereotype.Component;

@Component
public class BookingResponseMapper {

    public BookingResponse toResponse(Booking booking) {
        return new BookingResponse(
                booking.id(),
                booking.resourceId(),
                booking.userId(),
                booking.timeRange().start(),
                booking.timeRange().end(),
                booking.status().name()
        );
    }

    public PagedResponse<BookingResponse> toPagedResponse(
            PageResult<Booking> page
    ) {
        return new PagedResponse<>(
                page.content().stream()
                        .map(this::toResponse)
                        .toList(),
                page.page(),
                page.size(),
                page.totalElements(),
                page.totalPages(),
                page.first(),
                page.last()
        );
    }
}