package com.enzobersano.booking_platform_api.resource.application;

import com.enzobersano.booking_platform_api.resource.application.command.CreateResourceCommand;
import com.enzobersano.booking_platform_api.resource.application.port.ResourceRepositoryPort;
import com.enzobersano.booking_platform_api.resource.domain.failure.ResourceFailure;
import com.enzobersano.booking_platform_api.resource.domain.model.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateResourceUseCaseTest {

    private ResourceRepositoryPort repository;
    private CreateResourceUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(ResourceRepositoryPort.class);
        useCase = new CreateResourceUseCase(repository);
    }

    @Test
    void shouldCreateResourceSuccessfully() {
        var command = new CreateResourceCommand("Flight A", "FLIGHT");

        when(repository.save(any(Resource.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var result = useCase.execute(command);

        assertTrue(result.isSuccess());
        verify(repository).save(any(Resource.class));
    }

    @Test
    void shouldFailWhenTypeIsInvalid() {
        var command = new CreateResourceCommand("Flight D", "INVALID");

        var result = useCase.execute(command);

        assertFalse(result.isSuccess());
        assertInstanceOf(ResourceFailure.InvalidType.class, result.error());

        verify(repository, never()).save(any());
    }
}