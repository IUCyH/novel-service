package com.iucyh.novelservice.repository.novel.query.pagingquery.helper;

import com.iucyh.novelservice.domain.novel.Novel;
import com.iucyh.novelservice.repository.novel.NovelRepository;

import java.util.Comparator;

public class NovelPagingQueryTestHelper {

    private NovelPagingQueryTestHelper() {}

    public static Novel getLargestIdNovel(NovelRepository novelRepository) {
        return novelRepository.findAll()
                .stream()
                .max(Comparator.comparing(Novel::getId))
                .orElseThrow(() -> new IllegalArgumentException("Novel not found"));
    }
}
