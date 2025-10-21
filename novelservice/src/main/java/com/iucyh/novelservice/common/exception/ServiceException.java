package com.iucyh.novelservice.common.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public abstract class ServiceException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Object causes;

    protected ServiceException(ErrorCode errorCode, String message, Object causes) {
        super(message);
        this.errorCode = errorCode;
        this.causes = causes;
    }

    protected ServiceException(ErrorCode errorCode) {
        this(errorCode, errorCode.getDefaultMessage(), Map.of());
    }

    protected ServiceException(ErrorCode errorCode, Object causes) {
        this(errorCode, errorCode.getDefaultMessage(), causes);
    }

    protected ServiceException(ErrorCode errorCode, String message) {
        this(errorCode, message, Map.of());
    }
}
