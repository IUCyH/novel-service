package com.iucyh.novelservice.common.exception.novel;

import com.iucyh.novelservice.common.exception.ServiceException;

import java.util.Map;

public class DuplicateNovelTitle extends ServiceException {

    public DuplicateNovelTitle(String title) {
        super(
                NovelErrorCode.TITLE_DUPLICATE,
                "This title already exists",
                Map.of("title", title)
        );
    }
}
