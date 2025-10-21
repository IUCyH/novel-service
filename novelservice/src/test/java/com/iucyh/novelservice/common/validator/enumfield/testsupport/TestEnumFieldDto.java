package com.iucyh.novelservice.common.validator.enumfield.testsupport;

import com.iucyh.novelservice.common.testsupport.TestValuedEnum;
import com.iucyh.novelservice.common.validator.enumfield.EnumField;

public class TestEnumFieldDto {

    private String name;

    @EnumField(enumClass = TestValuedEnum.class)
    private String testEnumString;

    public TestEnumFieldDto() {}

    public TestEnumFieldDto(String name, String testEnum) {
        this.name = name;
        this.testEnumString = testEnum;
    }

    public String getName() {
        return name;
    }

    public String getTestEnumString() {
        return testEnumString;
    }
}
