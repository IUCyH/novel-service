package com.iucyh.novelservice.dto;

import lombok.Getter;

@Getter
public class SuccessDto<T> {

    private final Boolean isSuccess = true;
    private final T data;

    public SuccessDto(T data) {
        this.data = data;
    }

    public static <T> SuccessDto<T> empty() {
        return new SuccessDto<>(null);
    }
}
