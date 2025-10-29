package com.iucyh.novelservice.testsupport.testfactory.novel;

import com.iucyh.novelservice.domain.novel.Novel;
import com.iucyh.novelservice.domain.novel.NovelCategory;
import org.springframework.test.util.ReflectionTestUtils;

public class NovelEntityTestFactory {

    private NovelEntityTestFactory() {}

    public static Novel defaultNovelWithId() {
        Novel novel = Novel.of("test", "test desc", NovelCategory.ETC);
        fillId(novel);
        return novel;
    }

    public static Novel defaultNovelWithId(String title) {
        Novel novel = Novel.of(title, "test desc", NovelCategory.ETC);
        fillId(novel);
        return novel;
    }

    public static Novel oldNovel() {
        return Novel.of("old", "old desc", NovelCategory.ETC);
    }

    public static Novel oldNovelWithId() {
        Novel novel = Novel.of("old", "old desc", NovelCategory.ETC);
        fillId(novel);
        return novel;
    }

    private static void fillId(Novel novel) {
        ReflectionTestUtils.setField(novel, "id", 1L);
    }
}
