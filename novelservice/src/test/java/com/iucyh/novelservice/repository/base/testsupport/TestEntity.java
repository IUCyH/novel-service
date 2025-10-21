package com.iucyh.novelservice.repository.base.testsupport;

import com.iucyh.novelservice.domain.PublicEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class TestEntity extends PublicEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public TestEntity() {}

    public TestEntity(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public UUID getPublicId() {
        return super.getPublicId();
    }

    public String getName() {
        return name;
    }
}
