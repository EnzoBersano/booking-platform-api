package com.enzobersano.booking_platform_api.resource.application;

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

class GetResourceByIdUseCaseTest {

    private ResourceRepositoryPort repository;
    private GetResourceByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(ResourceRepositoryPort.class);
        useCase = new GetResourceByIdUseCase(repository);
    }

    @Test
    void shouldReturnResourceWhenFound() {
        var id = UUID.randomUUID();
        var resource = Resource.create("Flight 5", ResourceType.FLIGHT);

        when(repository.findById(id)).thenReturn(Optional.of(resource));

        var result = useCase.execute(id);

        assertTrue(result.isSuccess());
        assertEquals(resource, result.value());
    }

    @Test
    void shouldFailWhenNotFound() {
        var id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        var result = useCase.execute(id);

        assertFalse(result.isSuccess());
        assertInstanceOf(ResourceFailure.NotFound.class, result.error());
    }
}