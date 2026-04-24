package com.enzobersano.booking_platform_api.resource.application.port;

import com.enzobersano.booking_platform_api.resource.domain.model.Resource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ResourceRepositoryPort {

    Resource save(Resource resource);

    Optional<Resource> findById(UUID id);

    List<Resource> findAll();
}