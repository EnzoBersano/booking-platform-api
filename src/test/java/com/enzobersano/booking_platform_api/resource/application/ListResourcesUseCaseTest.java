package com.enzobersano.booking_platform_api.resource.application;

import com.enzobersano.booking_platform_api.resource.api.dto.ListResourcesRequest;
import com.enzobersano.booking_platform_api.resource.api.mapper.ListResourcesRequestMapper;
import com.enzobersano.booking_platform_api.resource.application.port.ResourceRepositoryPort;
import com.enzobersano.booking_platform_api.resource.application.query.ListResourcesQuery;
import com.enzobersano.booking_platform_api.resource.domain.failure.ResourceFailure;
import com.enzobersano.booking_platform_api.resource.domain.model.Resource;
import com.enzobersano.booking_platform_api.resource.domain.model.ResourceSortBy;
import com.enzobersano.booking_platform_api.resource.domain.model.ResourceSortDirection;
import com.enzobersano.booking_platform_api.resource.domain.model.ResourceStatus;
import com.enzobersano.booking_platform_api.resource.domain.model.ResourceType;
import com.enzobersano.booking_platform_api.shared.pagination.PageResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListResourcesUseCaseTest {

    private ResourceRepositoryPort repository;
    private ListResourcesUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(ResourceRepositoryPort.class);
        useCase = new ListResourcesUseCase(repository);
    }

    @Test
    void shouldReturnPagedResources() {
        var query = new ListResourcesQuery(
                0,
                10,
                ResourceSortBy.NAME,
                ResourceSortDirection.ASC
        );

        PageResult<Resource> page = new PageResult<>(
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
    void shouldReturnResourcesWithContent() {
        var query = new ListResourcesQuery(
                0,
                10,
                ResourceSortBy.NAME,
                ResourceSortDirection.ASC
        );

        var resource = Resource.reconstitute(
                UUID.randomUUID(),
                "Service 1",
                ResourceType.SERVICE,
                ResourceStatus.ACTIVE
        );

        PageResult<Resource> page = new PageResult<>(
                List.of(resource),
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
        assertEquals("Service 1", result.value().content().get(0).name());
        assertEquals(1, result.value().totalElements());
    }

    @Test
    void shouldPassSortingAndPaginationToRepository() {
        var query = new ListResourcesQuery(
                2,
                5,
                ResourceSortBy.STATUS,
                ResourceSortDirection.DESC
        );

        PageResult<Resource> page = new PageResult<>(
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
        assertFalse(result.value().first());
        assertTrue(result.value().last());
    }
    @Test
    void shouldReturnFailureWhenSortByIsInvalid() {
        var request = new ListResourcesRequest(
                0,
                10,
                "invalidField",
                "asc"
        );

        var mapper = new ListResourcesRequestMapper();

        var result = mapper.toQuery(request);

        assertFalse(result.isSuccess());
        assertTrue(result.error() instanceof ResourceFailure.InvalidSortBy);
        assertEquals(
                "Invalid sort field: invalidField",
                result.error().message()
        );
    }

    @Test
    void shouldReturnFailureWhenDirectionIsInvalid() {
        var request = new ListResourcesRequest(
                0,
                10,
                "name",
                "sideways"
        );

        var mapper = new ListResourcesRequestMapper();

        var result = mapper.toQuery(request);

        assertFalse(result.isSuccess());
        assertTrue(result.error() instanceof ResourceFailure.InvalidSortDirection);
        assertEquals(
                "Invalid sort direction: sideways. Allowed: asc, desc",
                result.error().message()
        );
    }
}