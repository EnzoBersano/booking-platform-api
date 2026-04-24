package com.enzobersano.booking_platform_api.resource.application;

import com.enzobersano.booking_platform_api.resource.application.command.CreateResourceCommand;
import com.enzobersano.booking_platform_api.resource.application.port.ResourceRepositoryPort;
import com.enzobersano.booking_platform_api.resource.domain.failure.ResourceFailure;
import com.enzobersano.booking_platform_api.resource.domain.model.Resource;
import com.enzobersano.booking_platform_api.resource.domain.model.ResourceType;
import com.enzobersano.booking_platform_api.shared.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateResourceUseCase {

    private final ResourceRepositoryPort repository;

    public CreateResourceUseCase(ResourceRepositoryPort repository) {
        this.repository = repository;
    }

    @Transactional
    public Result<Resource, ResourceFailure> execute(CreateResourceCommand command) {

        var typeResult = ResourceType.from(command.type());
        if (!typeResult.isSuccess()) {
            return Result.failure(
                    new ResourceFailure.InvalidType(command.type())
            );
        }

        var resource = Resource.create(
                command.name(),
                typeResult.value()
        );

        var saved = repository.save(resource);

        return Result.success(saved);
    }
}
