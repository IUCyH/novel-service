package com.iucyh.novelservice.domain;

import com.fasterxml.uuid.Generators;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

import java.util.UUID;

@MappedSuperclass
@Getter
public abstract class PublicEntity extends DateEntity {

    @Column(
            columnDefinition = "BINARY(16)",
            unique = true, nullable = false, updatable = false
    )
    private UUID publicId = Generators.timeBasedEpochGenerator().generate();
}
