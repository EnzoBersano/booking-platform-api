package com.enzobersano.booking_platform_api.resource.application;

import com.enzobersano.booking_platform_api.resource.application.command.DisableResourceCommand;
import com.enzobersano.booking_platform_api.resource.application.port.ResourceRepositoryPort;
import com.enzobersano.booking_platform_api.resource.domain.failure.ResourceFailure;
import com.enzobersano.booking_platform_api.shared.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DisableResourceUseCase {

    private final ResourceRepositoryPort repository;

    public DisableResourceUseCase(ResourceRepositoryPort repository) {
        this.repository = repository;
    }

    @Transactional
    public Result<Void, ResourceFailure> execute(DisableResourceCommand command) {

        var resourceOpt = repository.findById(command.resourceId());

        if (resourceOpt.isEmpty()) {
            return Result.failure(new ResourceFailure.NotFound());
        }

        var resource = resourceOpt.get();

        resource.disable();

        repository.save(resource);

        return Result.success(null);
    }
}
