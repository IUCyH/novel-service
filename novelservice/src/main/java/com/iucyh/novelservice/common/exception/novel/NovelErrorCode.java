package com.iucyh.novelservice.common.exception.novel;

import com.iucyh.novelservice.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NovelErrorCode implements ErrorCode {

    NOVEL_NOT_FOUND(HttpStatus.NOT_FOUND, "NOVEL-4041", "Novel not found with this id"),
    DUPLICATE_TITLE(HttpStatus.BAD_REQUEST, "NOVEL-4001", "Novel title already exists"),
    INVALID_CURSOR(HttpStatus.BAD_REQUEST, "NOVEL-4002", "Invalid paging cursor");

    private final HttpStatus status;
    private final String code;
    private final String defaultMessage;
}
