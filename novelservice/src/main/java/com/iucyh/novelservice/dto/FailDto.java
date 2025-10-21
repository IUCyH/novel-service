package com.iucyh.novelservice.dto;

import com.iucyh.novelservice.common.exception.ErrorCode;
import com.iucyh.novelservice.common.exception.ServiceException;
import lombok.Getter;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
public class FailDto {

    private final boolean isSuccess = false;
    private final Instant timestamp;
    private final int status;
    private final String code;
    private final String message;
    private final String path;
    private final Object causes;

    public FailDto(ErrorCode errorCode, String message, String path, Map<String, Object> causes) {
        this.timestamp = getCurrentTimeStamp();
        this.status = errorCode.getStatus().value();
        this.code = errorCode.getCode();
        this.message = message;
        this.path = path;
        this.causes = causes == null ? Map.of() : causes;
    }

    public FailDto(ErrorCode errorCode, String message, String path, List<LinkedHashMap<String, String>> causes) {
        this.timestamp = getCurrentTimeStamp();
        this.status = errorCode.getStatus().value();
        this.code = errorCode.getCode();
        this.message = message;
        this.path = path;
        this.causes = causes == null ? List.of() : causes;
    }

    public FailDto(ErrorCode errorCode, String message, String path) {
        this(errorCode, message, path, Map.of());
    }

    public static FailDto from(ServiceException e, String path) {
        return new FailDto(e.getErrorCode(), e.getMessage(), path, e.getCauses());
    }

    private Instant getCurrentTimeStamp() {
        return Instant.now();
    }
}
