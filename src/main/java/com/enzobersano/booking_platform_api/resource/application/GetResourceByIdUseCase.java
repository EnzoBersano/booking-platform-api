package com.enzobersano.booking_platform_api.resource.application;

import com.enzobersano.booking_platform_api.resource.application.port.ResourceRepositoryPort;
import com.enzobersano.booking_platform_api.resource.domain.failure.ResourceFailure;
import com.enzobersano.booking_platform_api.resource.domain.model.Resource;
import com.enzobersano.booking_platform_api.shared.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class GetResourceByIdUseCase {

    private final ResourceRepositoryPort repository;

    public GetResourceByIdUseCase(ResourceRepositoryPort repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Result<Resource, ResourceFailure> execute(UUID id) {

        var resourceOpt = repository.findById(id);

        if (resourceOpt.isEmpty()) {
            return Result.failure(new ResourceFailure.NotFound());
        }

        return Result.success(resourceOpt.get());
    }


}
