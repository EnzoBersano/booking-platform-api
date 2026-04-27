package com.enzobersano.booking_platform_api.booking.application;

import com.enzobersano.booking_platform_api.booking.application.port.BookingRepositoryPort;
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
class GetBookingByIdUseCaseTest {

    @Mock
    BookingRepositoryPort repository;
    @InjectMocks GetBookingByIdUseCase useCase;

    @Test
    void shouldReturnNotFound() {
        var id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        var result = useCase.execute(id);

        assertFalse(result.isSuccess());
    }

    @Test
    void shouldReturnBooking() {
        var booking = mock(Booking.class);
        var id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.of(booking));

        var result = useCase.execute(id);

        assertTrue(result.isSuccess());
        assertEquals(booking, result.value());
    }
}
