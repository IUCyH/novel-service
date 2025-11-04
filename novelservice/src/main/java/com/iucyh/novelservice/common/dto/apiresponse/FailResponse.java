package com.iucyh.novelservice.common.dto.apiresponse;

import com.iucyh.novelservice.common.exception.errorcode.ErrorCode;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;

@Getter
public class FailResponse {

    private final Boolean isSuccess = false;
    private final Instant timestamp;
    private final int status;
    private final String code;
    private final String message;
    private final String path;
    private final Map<String, Object> causes;

    protected FailResponse(ErrorCode errorCode, String message, String path, Map<String, Object> causes) {
        this.timestamp = getCurrentTimeStamp();
        this.status = errorCode.getStatus().value();
        this.code = errorCode.getCode();
        this.message = message;
        this.path = path;
        this.causes = causes == null ? Map.of() : causes;
    }

    private Instant getCurrentTimeStamp() {
        return Instant.now();
    }
}
