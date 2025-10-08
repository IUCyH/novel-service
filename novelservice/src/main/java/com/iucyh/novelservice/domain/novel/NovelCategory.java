package com.iucyh.novelservice.domain.novel;

import com.fasterxml.jackson.annotation.JsonValue;

public enum NovelCategory {

    FANTASY("fantasy"),
    ROMANCE("romance"),
    HORROR("horror"),
    SF("sf"),
    SPORTS("sports"),
    LIFE("life"),
    ETC("etc");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    NovelCategory(String value) {
        this.value = value;
    }

    public static NovelCategory of(String value) {
        if (value == null || value.isBlank()) return null;

        for (NovelCategory category : values()) {
            boolean isValueMatch = category.getValue().equalsIgnoreCase(value);
            boolean isNameMatch = category.name().equalsIgnoreCase(value);

            if (isValueMatch || isNameMatch) {
                return category;
            }
        }
        return null;
    }
}
