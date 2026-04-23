package com.enzobersano.booking_platform_api.shared.result;

public interface Result<T> {

    boolean isSuccess();
    T getValue();
    Error getError();

    static <T> Result<T> success(T value) {
        return new Success<>(value);
    }

    static <T> Result<T> failure(Error error) {
        return new Failure<>(error);
    }
}