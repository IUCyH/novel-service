package com.iucyh.novelservice.dto;

import lombok.Getter;

@Getter
public class IdDto {

    private final String publicId;

    public IdDto(String publicId) {
        this.publicId = publicId;
    }
}
