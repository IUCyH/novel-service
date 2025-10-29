package com.iucyh.novelservice.common.exception.testsupport;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class TestBodyDto {

    @NotEmpty
    @Size(max = 10)
    private String name;

    private Integer age;

    public TestBodyDto() {}

    public TestBodyDto(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }
}
