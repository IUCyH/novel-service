package com.iucyh.novelservice.common.enumtype.valuedenum;

public class ValuedEnumHelper {

    private ValuedEnumHelper() {}

    public static <T extends Enum<T> & ValuedEnum> T fromValue(String value, Class<T> enumClass) {
        if (value == null || value.isBlank()) return null;

        for (T e : enumClass.getEnumConstants()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
