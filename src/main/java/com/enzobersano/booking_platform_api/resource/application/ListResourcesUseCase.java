package com.enzobersano.booking_platform_api.resource.application;

import com.enzobersano.booking_platform_api.resource.application.port.ResourceRepositoryPort;
import com.enzobersano.booking_platform_api.resource.domain.model.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ListResourcesUseCase {

    private final ResourceRepositoryPort repository;

    public ListResourcesUseCase(ResourceRepositoryPort repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Resource> execute() {
        return repository.findAll();
    }
}
