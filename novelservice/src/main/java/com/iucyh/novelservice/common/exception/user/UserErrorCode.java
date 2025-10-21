package com.iucyh.novelservice.common.exception.user;

import com.iucyh.novelservice.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-404", "User Not Found With This Public Id");

    private final HttpStatus status;
    private final String code;
    private final String defaultMessage;
}
