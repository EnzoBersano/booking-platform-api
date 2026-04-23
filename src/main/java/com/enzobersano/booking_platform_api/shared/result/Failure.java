package com.enzobersano.booking_platform_api.shared.result;

public final class Failure<T> implements Result<T> {

    private final Error error;

    public Failure(Error error) {
        this.error = error;
    }

    @Override public boolean isSuccess() { return false; }
    @Override public Error getError()    { return error; }

    @Override
    public T getValue() {
        throw new IllegalStateException("No value in failure");
    }
}