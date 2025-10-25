package com.iucyh.novelservice.common.exception.testsupport;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test-con")
public class TestController {

    @GetMapping("/path-variable/{variable}")
    public ResponseEntity<Integer> pathVariable(@PathVariable Integer variable) {
        return ResponseEntity.ok(variable);
    }

    @GetMapping("/request-parameter")
    public ResponseEntity<String> requestParameter(@RequestParam String variable) {
        return ResponseEntity.ok(variable);
    }

    @PostMapping("/request-body")
    public ResponseEntity<String> requestBody(@Valid @RequestBody TestBodyDto body) {
        return ResponseEntity.ok(body.getName());
    }
}
