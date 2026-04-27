package com.enzobersano.booking_platform_api.booking.application;

import com.enzobersano.booking_platform_api.booking.application.port.BookingRepositoryPort;
import com.enzobersano.booking_platform_api.booking.application.query.BookingSortBy;
import com.enzobersano.booking_platform_api.booking.application.query.BookingSortDirection;
import com.enzobersano.booking_platform_api.booking.application.query.ListBookingsQuery;
import com.enzobersano.booking_platform_api.booking.domain.model.Booking;
import com.enzobersano.booking_platform_api.booking.domain.model.BookingStatus;
import com.enzobersano.booking_platform_api.booking.domain.valueobject.TimeRange;
import com.enzobersano.booking_platform_api.shared.pagination.PageResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ListBookingsUseCaseTest {

    @Mock
    BookingRepositoryPort repository;
    @InjectMocks ListBookingsUseCase useCase;

    @Test
    void shouldReturnPageResult() {

        var query = new ListBookingsQuery(0, 10, null, null);

        var page = new PageResult<Booking>(
                List.of(),
                0,
                10,
                0,
                0,
                true,
                true
        );

        when(repository.findAll(query)).thenReturn(page);

        var result = useCase.execute(query);

        assertTrue(result.isSuccess());
        assertEquals(page, result.value());
    }

    @Test
    void shouldReturnBookingsWithContent() {

        var query = new ListBookingsQuery(0, 10, null, null);

        var booking = Booking.reconstitute(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                new TimeRange(
                        Instant.now().plusSeconds(3600),
                        Instant.now().plusSeconds(7200)
                ),
                BookingStatus.ACTIVE
        );

        var page = new PageResult<>(
                List.of(booking),
                0,
                10,
                1,
                1,
                true,
                true
        );

        when(repository.findAll(query)).thenReturn(page);

        var result = useCase.execute(query);

        assertTrue(result.isSuccess());
        assertEquals(1, result.value().content().size());
        assertEquals(booking.id(), result.value().content().get(0).id());
        assertEquals(1, result.value().totalElements());
    }

    @Test
    void shouldPassPaginationToRepository() {

        var query = new ListBookingsQuery(2, 5, null, null);

        var page = new PageResult<Booking>(
                List.of(),
                2,
                5,
                0,
                0,
                false,
                true
        );

        when(repository.findAll(query)).thenReturn(page);

        var result = useCase.execute(query);

        assertTrue(result.isSuccess());

        verify(repository).findAll(query);

        assertEquals(2, result.value().page());
        assertEquals(5, result.value().size());
    }

    @Test
    void shouldPassSortingParameters() {

        var query = new ListBookingsQuery(
                1,
                10,
                BookingSortBy.START_TIME,
                BookingSortDirection.ASC
        );

        var page = new PageResult<Booking>(
                List.of(),
                1,
                10,
                0,
                0,
                false,
                false
        );

        when(repository.findAll(query)).thenReturn(page);

        var result = useCase.execute(query);

        assertTrue(result.isSuccess());

        verify(repository).findAll(query);
    }

    @Test
    void shouldReturnEmptyPage() {

        var query = new ListBookingsQuery(0, 10, null, null);

        var page = new PageResult<Booking>(
                List.of(),
                0,
                10,
                0,
                0,
                true,
                true
        );

        when(repository.findAll(query)).thenReturn(page);

        var result = useCase.execute(query);

        assertTrue(result.isSuccess());
        assertTrue(result.value().content().isEmpty());
    }
}
