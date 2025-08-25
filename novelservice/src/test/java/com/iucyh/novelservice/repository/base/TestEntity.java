package com.iucyh.novelservice.repository.base;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class TestEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String publicId = UUID.randomUUID().toString().replace("-", "");

    private String name;

    public TestEntity() {}

    public TestEntity(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getPublicId() {
        return publicId;
    }

    public String getName() {
        return name;
    }
}
