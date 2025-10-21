package com.iucyh.novelservice.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-5001", "Internal Server Error"),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "COMMON-4001", "Validation Failed"),
    MISSING_PATH_VARIABLE(HttpStatus.BAD_REQUEST, "COMMON-4002", "Missing Path Variable"),
    HTTP_MESSAGE_NOT_READABLE(HttpStatus.BAD_REQUEST, "COMMON-4003", "Cannot Read Request Body"),
    METHOD_ARGUMENT_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "COMMON-4004", "Argument Type Mismatch");

    private final HttpStatus status;
    private final String code;
    private final String defaultMessage;
}
