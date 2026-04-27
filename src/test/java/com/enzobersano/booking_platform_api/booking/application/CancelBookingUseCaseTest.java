package com.enzobersano.booking_platform_api.booking.application;

import com.enzobersano.booking_platform_api.booking.application.command.CancelBookingCommand;
import com.enzobersano.booking_platform_api.booking.application.port.BookingRepositoryPort;
import com.enzobersano.booking_platform_api.booking.domain.failure.BookingFailure;
import com.enzobersano.booking_platform_api.booking.domain.model.Booking;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CancelBookingUseCaseTest {

    @Mock
    BookingRepositoryPort repository;
    @InjectMocks CancelBookingUseCase useCase;

    @Test
    void shouldReturnNotFound() {
        var id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        var result = useCase.execute(new CancelBookingCommand(id));

        assertFalse(result.isSuccess());
        assertInstanceOf(BookingFailure.NotFound.class, result.error());
    }

    @Test
    void shouldCancelBooking() {
        var booking = mock(Booking.class);
        var id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.of(booking));

        var result = useCase.execute(new CancelBookingCommand(id));

        assertTrue(result.isSuccess());
        verify(booking).cancel();
        verify(repository).save(booking);
    }
}
