package com.iucyh.novelservice.common.enumtype;

public interface ValuedEnum {

    String getValue();

    default <T extends Enum<T> & ValuedEnum> T fromValue(String value, Class<T> enumClass) {
        if (value == null || value.isBlank()) return null;

        for (T e : enumClass.getEnumConstants()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
