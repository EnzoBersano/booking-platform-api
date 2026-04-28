package com.enzobersano.booking_platform_api.resource.api.mapper;

import com.enzobersano.booking_platform_api.resource.api.dto.ListResourcesRequest;
import com.enzobersano.booking_platform_api.resource.application.query.ListResourcesQuery;
import com.enzobersano.booking_platform_api.resource.domain.failure.ResourceFailure;
import com.enzobersano.booking_platform_api.resource.application.query.ResourceSortBy;
import com.enzobersano.booking_platform_api.resource.application.query.ResourceSortDirection;
import com.enzobersano.booking_platform_api.shared.result.Result;
import org.springframework.stereotype.Component;

@Component
public class ListResourcesRequestMapper {

    public Result<ListResourcesQuery, ResourceFailure> toQuery(
            ListResourcesRequest request
    ) {

        var sortByResult = ResourceSortBy.from(request.sortByOrDefault());

        if (!sortByResult.isSuccess()) {
            return Result.failure(sortByResult.error());
        }

        var directionResult =
                ResourceSortDirection.from(request.directionOrDefault());

        if (!directionResult.isSuccess()) {
            return Result.failure(directionResult.error());
        }

        return Result.success(
                new ListResourcesQuery(
                        request.pageOrDefault(),
                        request.sizeOrDefault(),
                        sortByResult.value(),
                        directionResult.value()
                )
        );
    }
}