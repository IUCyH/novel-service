package com.iucyh.novelservice.common.dto.apiresponse;

import com.iucyh.novelservice.common.dto.apiresponse.information.FailInformation;
import com.iucyh.novelservice.common.exception.ServiceException;

import java.util.Map;

public class ApiResponseMapper {

    private ApiResponseMapper() {}

    public static FailResponse fail(FailInformation info, Map<String, Object> causes) {
        return new FailResponse(info.errorCode(), info.message(), info.path(), causes);
    }

    public static FailResponse fail(FailInformation info) {
        return new FailResponse(info.errorCode(), info.message(), info.path(), null);
    }

    public static FailResponse fail(ServiceException ex, String path) {
        return new FailResponse(ex.getErrorCode(), ex.getMessage(), path, ex.getCauses());
    }

    public static SuccessResponse<Void> success() {
        return new SuccessResponse<>(null);
    }

    public static <T> SuccessResponse<T> success(T data) {
        return new SuccessResponse<>(data);
    }
}
