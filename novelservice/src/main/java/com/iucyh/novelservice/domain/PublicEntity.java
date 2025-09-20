package com.iucyh.novelservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

import java.util.UUID;

@MappedSuperclass
@Getter
public abstract class PublicEntity extends DateEntity {

    @Column(nullable = false, unique = true, updatable = false, length = 32)
    private String publicId = UUID.randomUUID().toString().replace("-", "");
}
