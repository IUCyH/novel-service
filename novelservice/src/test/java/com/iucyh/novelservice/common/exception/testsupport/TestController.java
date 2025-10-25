package com.iucyh.novelservice.common.exception;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestController
@RequestMapping("/test-con")
public class TestController {

    @GetMapping("/method-argument-type-mismatch")
    public void methodArgumentTypeMismatch() {
    }
}
