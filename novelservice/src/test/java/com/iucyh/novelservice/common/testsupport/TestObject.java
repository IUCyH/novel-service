package com.iucyh.novelservice.common.testsupport;

public class TestObject {

    private String name;
    private Integer age;

    public TestObject() {}

    public TestObject(String name, Integer age) {
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
