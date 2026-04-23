package com.enzobersano.booking_platform_api.shared.result;

public final class FailureResult<T, E extends Failure> implements Result<T, E> {

    private final E error;

    public FailureResult(E error) {
        this.error = error;
    }

    @Override public boolean isSuccess() { return false; }

    @Override public T value() {
        throw new IllegalStateException("No value in failure");
    }

    @Override public E error() { return error; }
}