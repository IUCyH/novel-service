package com.iucyh.novelservice.common.exception.novel;

import com.iucyh.novelservice.common.exception.ErrorCode;
import com.iucyh.novelservice.common.exception.ServiceException;

import java.util.Map;

public class InvalidNovelCursor extends ServiceException {

    public InvalidNovelCursor() {
        super(NovelErrorCode.INVALID_CURSOR, "Invalid cursor", null);
    }
}
