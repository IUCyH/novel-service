package com.iucyh.novelservice.domain.novel;

import com.fasterxml.jackson.annotation.JsonValue;
import com.iucyh.novelservice.common.enumtype.ValuedEnum;

public enum NovelCategory implements ValuedEnum {

    FANTASY("fantasy"),
    ROMANCE("romance"),
    HORROR("horror"),
    SF("sf"),
    SPORTS("sports"),
    LIFE("life"),
    ETC("etc");

    private final String value;

    NovelCategory(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String getValue() {
        return value;
    }

    public static NovelCategory of(String value) {
        if (value == null || value.isBlank()) return null;

        for (NovelCategory category : values()) {
            if (category.getValue().equals(value)) {
                return category;
            }
        }
        return null;
    }
}
