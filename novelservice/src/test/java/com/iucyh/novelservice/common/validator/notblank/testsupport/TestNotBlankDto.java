package com.iucyh.novelservice.common.validator.notblank.testsupport;

import com.iucyh.novelservice.common.validator.notblank.NotBlankIfPresent;

public class TestNotBlankDto {

    @NotBlankIfPresent
    private String name;

    public TestNotBlankDto() {}

    public TestNotBlankDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
