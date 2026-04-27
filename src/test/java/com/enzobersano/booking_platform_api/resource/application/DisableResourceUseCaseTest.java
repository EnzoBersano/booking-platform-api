package com.enzobersano.booking_platform_api.resource.application;

import com.enzobersano.booking_platform_api.resource.application.command.DisableResourceCommand;
import com.enzobersano.booking_platform_api.resource.application.port.ResourceRepositoryPort;
import com.enzobersano.booking_platform_api.resource.domain.failure.ResourceFailure;
import com.enzobersano.booking_platform_api.resource.domain.model.Resource;
import com.enzobersano.booking_platform_api.resource.domain.model.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DisableResourceUseCaseTest {

    private ResourceRepositoryPort repository;
    private DisableResourceUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(ResourceRepositoryPort.class);
        useCase = new DisableResourceUseCase(repository);
    }

    @Test
    void shouldDisableResourceSuccessfully() {
        var id = UUID.randomUUID();
        var resource = Resource.create("Room A", ResourceType.ROOM);

        when(repository.findById(id)).thenReturn(Optional.of(resource));

        var result = useCase.execute(new DisableResourceCommand(id));

        assertTrue(result.isSuccess());
        verify(repository).save(resource);
    }

    @Test
    void shouldFailWhenResourceNotFound() {
        var id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        var result = useCase.execute(new DisableResourceCommand(id));

        assertFalse(result.isSuccess());
        assertInstanceOf(ResourceFailure.NotFound.class, result.error());

        verify(repository, never()).save(any());
    }
}