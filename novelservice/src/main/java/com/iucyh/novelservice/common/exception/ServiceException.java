package com.iucyh.novelservice.common.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public abstract class ServiceException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Map<String, Object> causes;

    protected ServiceException(ErrorCode errorCode, String message, Map<String, Object> causes) {
        super(message);
        this.errorCode = errorCode;
        this.causes = causes;
    }
}
