package com.enzobersano.booking_platform_api.shared.result;

public interface Result<T, E extends Failure> {

    boolean isSuccess();

    T value();
    E error();

    static <T, E extends Failure> Result<T, E> success(T value) {
        return new Success<>(value);
    }

    static <T, E extends Failure> Result<T, E> failure(E error) {
        return new FailureResult<>(error);
    }
}