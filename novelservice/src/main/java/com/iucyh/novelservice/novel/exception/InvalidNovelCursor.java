package com.iucyh.novelservice.novel.exception;

import com.iucyh.novelservice.common.exception.ServiceException;
import com.iucyh.novelservice.novel.exception.errorcode.NovelErrorCode;

public class InvalidNovelCursor extends ServiceException {

    public InvalidNovelCursor() {
        super(NovelErrorCode.INVALID_CURSOR);
    }
}
