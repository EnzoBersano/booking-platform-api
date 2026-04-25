package com.enzobersano.booking_platform_api.resource.infrastructure.persistence;

import com.enzobersano.booking_platform_api.resource.application.port.ResourceRepositoryPort;
import com.enzobersano.booking_platform_api.resource.domain.model.Resource;
import com.enzobersano.booking_platform_api.resource.infrastructure.mapper.ResourceMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ResourceRepositoryAdapter implements ResourceRepositoryPort {

    private final ResourceJpaRepository jpa;
    private final ResourceMapper mapper;

    public ResourceRepositoryAdapter(ResourceJpaRepository jpa, ResourceMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public Resource save(Resource resource) {
        return mapper.toDomain(jpa.save(mapper.toEntity(resource)));
    }

    @Override
    public Optional<Resource> findById(UUID id) {
        return jpa.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Resource> findAll() {
        return jpa.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}