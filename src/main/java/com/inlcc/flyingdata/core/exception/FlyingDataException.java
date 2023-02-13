package com.inlcc.flyingdata.core.exception;

public class FlyingDataException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FlyingDataException(String message) {
        super(message);
    }

    public FlyingDataException(Throwable throwable) {
        super(throwable);
    }

    public FlyingDataException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
