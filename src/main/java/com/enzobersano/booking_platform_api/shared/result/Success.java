package com.enzobersano.booking_platform_api.shared.result;

public final class Success<T> implements Result<T> {

    private final T value;

    public Success(T value) {
        this.value = value;
    }

    @Override public boolean isSuccess() { return true; }
    @Override public T getValue()        { return value; }

    @Override
    public Error getError() {
        throw new IllegalStateException("No error in success");
    }
}