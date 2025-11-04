package com.iucyh.novelservice.common.dto.apiresponse;

import lombok.Getter;

@Getter
public class SuccessResponse<T> {

    private final Boolean isSuccess = true;
    private final T data;

    protected SuccessResponse(T data) {
        this.data = data;
    }
}
