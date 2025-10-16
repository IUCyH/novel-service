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

    /**
     * publicId 를 문자열로 변환 후 하이픈('-') 을 제거한 결과를 반환
     */
    public String getPublicIdToString() {
        return publicId.toString().replace("-", "");
    }
}
