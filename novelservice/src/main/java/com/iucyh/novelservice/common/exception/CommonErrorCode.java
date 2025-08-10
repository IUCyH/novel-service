package com.iucyh.novelservice.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-500"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON-400");

    private final HttpStatus status;
    private final String code;
}
