package com.enzobersano.booking_platform_api.booking.infrastructure.mapper;

import com.enzobersano.booking_platform_api.booking.domain.model.Booking;
import com.enzobersano.booking_platform_api.booking.domain.model.BookingStatus;
import com.enzobersano.booking_platform_api.booking.domain.valueobject.TimeRange;
import com.enzobersano.booking_platform_api.booking.infrastructure.persistence.BookingJpaEntity;
import com.enzobersano.booking_platform_api.resource.domain.model.Resource;
import com.enzobersano.booking_platform_api.resource.infrastructure.persistence.ResourceJpaEntity;
import com.enzobersano.booking_platform_api.shared.pagination.PageResult;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public BookingJpaEntity toEntity(Booking booking) {
        return new BookingJpaEntity(
                booking.id(),
                booking.resourceId(),
                booking.userId(),
                booking.timeRange().start(),
                booking.timeRange().end(),
                booking.status().name()
        );
    }

    public Booking toDomain(BookingJpaEntity entity) {
        return Booking.reconstitute(
                entity.getId(),
                entity.getResourceId(),
                entity.getUserId(),
                new TimeRange(
                        entity.getStartTime(),
                        entity.getEndTime()
                ),
                BookingStatus.valueOf(entity.getStatus())
        );
    }

    public PageResult<Booking> toPageResult(Page<BookingJpaEntity> page) {

        var content = page.getContent()
                .stream()
                .map(this::toDomain)
                .toList();

        return new PageResult<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }
}