package com.iucyh.novelservice.common.testsupport;

import com.iucyh.novelservice.common.enumtype.valuedenum.ValuedEnum;
import com.iucyh.novelservice.common.enumtype.valuedenum.ValuedEnumHelper;

public enum TestValuedEnum implements ValuedEnum {

    A("a"),
    B("b"),
    C("c");

    private final String value;

    TestValuedEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TestValuedEnum of(String value) {
        return ValuedEnumHelper.fromValue(value, TestValuedEnum.class);
    }
}
