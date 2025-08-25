package com.iucyh.novelservice.domain.novel;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum NovelCategory {

    FANTASY,
    ROMANCE,
    HORROR,
    SF,
    SPORTS,
    LIFE,
    ETC;

    @JsonCreator
    public static NovelCategory from(String value) {
        return Arrays.stream(NovelCategory.values())
                .filter(category -> category.name().equals(value))
                .findAny()
                .orElse(null);
    }
}
