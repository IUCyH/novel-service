package com.iucyh.novelservice.testsupport.testfactory.novel;

import com.iucyh.novelservice.domain.novel.Novel;
import com.iucyh.novelservice.domain.novel.NovelCategory;
import com.iucyh.novelservice.dto.novel.*;

import java.time.LocalDateTime;

public class NovelDtoTestFactory {

    private NovelDtoTestFactory() {}

    public static NovelDto defaultNovelDto() {
        return new NovelDto(
                1L,
                "test",
                "test desc",
                0,
                0,
                NovelCategory.ETC,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public static CreateNovelDto defaultCreateNovelDto() {
        return new CreateNovelDto(
                "test",
                "test desc",
                NovelCategory.ETC.getValue()
        );
    }

    public static UpdateNovelDto defaultUpdateNovelDto() {
        return new UpdateNovelDto(
                "new",
                "new desc",
                NovelCategory.ETC.getValue()
        );
    }
}
