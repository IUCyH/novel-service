package com.iucyh.novelservice.common.domain;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;

@MappedSuperclass
@Getter
public abstract class PublicEntity extends SoftDeleteEntity {

    @Column(
            length = 25,
            unique = true, nullable = false, updatable = false
    )
    private String publicId;

    @PrePersist
    protected void setPublicId() {
        if (publicId == null) {
            publicId = NanoIdUtils.randomNanoId();
        }
    }
}
