package com.enzobersano.booking_platform_api.booking.application;

import com.enzobersano.booking_platform_api.auth.application.port.CurrentUserPort;
import com.enzobersano.booking_platform_api.booking.application.command.CreateBookingCommand;
import com.enzobersano.booking_platform_api.booking.application.port.BookingRepositoryPort;
import com.enzobersano.booking_platform_api.booking.application.port.ResourceAvailabilityPort;
import com.enzobersano.booking_platform_api.booking.domain.failure.BookingFailure;
import com.enzobersano.booking_platform_api.booking.domain.policy.BookingPolicy;
import com.enzobersano.booking_platform_api.shared.result.Result;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateBookingUseCaseTest {

    @Mock BookingRepositoryPort repository;
    @Mock ResourceAvailabilityPort resourcePort;
    @Mock
    BookingPolicy bookingPolicy;
    @Mock CurrentUserPort currentUserPort;

    @InjectMocks
    CreateBookingUseCase useCase;

    @Test
    void shouldFailWhenResourceDoesNotExist() {
        var command = validCommand();

        when(resourcePort.exists(command.resourceId())).thenReturn(false);

        var result = useCase.execute(command);

        assertFalse(result.isSuccess());
        assertInstanceOf(BookingFailure.NotFound.class, result.error());

        verify(repository, never()).save(any());
    }

    @Test
    void shouldFailWhenResourceIsInactive() {

        var command = validCommand();

        when(resourcePort.exists(command.resourceId())).thenReturn(true);
        when(resourcePort.isActive(command.resourceId())).thenReturn(false);

        when(repository.findByResourceIdAndRange(any(), any(), any()))
                .thenReturn(List.of());

        when(bookingPolicy.validate(any(), anyList(), anyBoolean(), any()))
                .thenReturn(Result.failure(new BookingFailure.Conflict("", "inactive")));

        var result = useCase.execute(command);

        assertFalse(result.isSuccess());
    }

    @Test
    void shouldFailWhenPolicyRejectsBooking() {
        var command = validCommand();

        when(resourcePort.exists(command.resourceId())).thenReturn(true);
        when(resourcePort.isActive(command.resourceId())).thenReturn(true);
        when(repository.findByResourceIdAndRange(any(), any(), any()))
                .thenReturn(List.of());

        when(bookingPolicy.validate(any(), anyList(), anyBoolean(), any()))
                .thenReturn(Result.failure(new BookingFailure.Conflict("", "conflict")));

        var result = useCase.execute(command);

        assertFalse(result.isSuccess());
        verify(repository, never()).save(any());
    }

    @Test
    void shouldCreateBookingSuccessfully() {
        var command = validCommand();

        when(resourcePort.exists(command.resourceId())).thenReturn(true);
        when(resourcePort.isActive(command.resourceId())).thenReturn(true);
        when(repository.findByResourceIdAndRange(any(), any(), any()))
                .thenReturn(List.of());

        when(bookingPolicy.validate(any(), anyList(), anyBoolean(), any()))
                .thenReturn(Result.success(null));

        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        var result = useCase.execute(command);

        assertTrue(result.isSuccess());
        verify(repository).save(any());
    }

    private CreateBookingCommand validCommand() {
        return new CreateBookingCommand(
                UUID.randomUUID(),
                Instant.now().plusSeconds(3600),
                Instant.now().plusSeconds(7200)
        );
    }
}