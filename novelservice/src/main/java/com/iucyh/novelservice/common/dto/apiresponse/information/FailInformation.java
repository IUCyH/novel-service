package com.iucyh.novelservice.common.dto.apiresponse.information;

import com.iucyh.novelservice.common.exception.errorcode.ErrorCode;

public record FailInformation(

        ErrorCode errorCode,
        String message,
        String path
) {}
