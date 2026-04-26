package com.enzobersano.booking_platform_api.resource.domain.failure;

import com.enzobersano.booking_platform_api.shared.result.Failure;

public sealed interface ResourceFailure extends Failure
        permits ResourceFailure.InvalidSortBy, ResourceFailure.InvalidSortDirection, ResourceFailure.InvalidType, ResourceFailure.NotFound {

    record NotFound() implements ResourceFailure {
        public String message() { return "Resource not found"; }
    }
    record InvalidType(String value) implements ResourceFailure {
        public String message() {
            return "Invalid resource type: " + value;
        }
    }

    record InvalidSortBy(String value) implements ResourceFailure {
        public String message() {
            return "Invalid sort field: " + value;
        }
    }

    record InvalidSortDirection(String value) implements ResourceFailure {
        public String message() {
            return "Invalid sort direction: " + value + ". Allowed: asc, desc";
        }
    }
}