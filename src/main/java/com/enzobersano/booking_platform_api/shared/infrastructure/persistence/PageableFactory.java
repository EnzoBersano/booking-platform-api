package com.enzobersano.booking_platform_api.shared.infrastructure.persistence;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public final class PageableFactory {

    private PageableFactory() {}

    public static PageRequest create(
            int page,
            int size,
            String field,
            boolean desc
    ) {

        Sort.Direction direction =
                desc ? Sort.Direction.DESC : Sort.Direction.ASC;

        return PageRequest.of(
                page,
                size,
                Sort.by(direction, field)
        );
    }
}