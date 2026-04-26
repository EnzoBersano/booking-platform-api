package com.enzobersano.booking_platform_api.resource.application;

import com.enzobersano.booking_platform_api.resource.application.port.ResourceRepositoryPort;
import com.enzobersano.booking_platform_api.resource.application.query.ListResourcesQuery;
import com.enzobersano.booking_platform_api.resource.domain.failure.ResourceFailure;
import com.enzobersano.booking_platform_api.resource.domain.model.Resource;
import com.enzobersano.booking_platform_api.shared.pagination.PageResult;
import com.enzobersano.booking_platform_api.shared.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListResourcesUseCase {

    private final ResourceRepositoryPort repository;

    public ListResourcesUseCase(ResourceRepositoryPort repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Result<PageResult<Resource>, ResourceFailure> execute(ListResourcesQuery query) {

        return Result.success(repository.findAll(query));
    }
}
