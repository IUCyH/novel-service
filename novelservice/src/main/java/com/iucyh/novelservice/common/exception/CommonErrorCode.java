package com.iucyh.novelservice.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-5001", "Internal server error"),

    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "COMMON-4001", "Validation failed"),
    MISSING_PATH_VARIABLE(HttpStatus.BAD_REQUEST, "COMMON-4002", "Missing path variable"),
    HTTP_MESSAGE_NOT_READABLE(HttpStatus.BAD_REQUEST, "COMMON-4003", "Cannot read request body"),
    METHOD_ARGUMENT_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "COMMON-4004", "Method argument type mismatch"),
    MISSING_SERVLET_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "COMMON-4005", "Missing required request parameter"),

    NO_RESOURCE_FOUND(HttpStatus.NOT_FOUND, "COMMON-4041", "No resource found");

    private final HttpStatus status;
    private final String code;
    private final String defaultMessage;
}
