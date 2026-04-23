package com.enzobersano.booking_platform_api.shared.result;

public final class Success<T, E extends Failure> implements Result<T, E> {

    private final T value;

    public Success(T value) {
        this.value = value;
    }

    @Override public boolean isSuccess() { return true; }

    @Override public T value() { return value; }

    @Override public E error() {
        throw new IllegalStateException("No error in success");
    }
}