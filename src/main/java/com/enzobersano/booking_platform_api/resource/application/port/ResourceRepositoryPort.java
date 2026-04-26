package com.enzobersano.booking_platform_api.resource.application.port;

import com.enzobersano.booking_platform_api.resource.application.query.ListResourcesQuery;
import com.enzobersano.booking_platform_api.resource.domain.model.Resource;
import com.enzobersano.booking_platform_api.shared.pagination.PageResult;
import java.util.Optional;
import java.util.UUID;

public interface ResourceRepositoryPort {

    Resource save(Resource resource);

    Optional<Resource> findById(UUID id);

    PageResult<Resource> findAll(ListResourcesQuery query);
}