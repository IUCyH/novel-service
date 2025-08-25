package com.iucyh.novelservice.common.exception;

import com.iucyh.novelservice.dto.FailDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<FailDto> handleServiceException(ServiceException e, HttpServletRequest req) {
        log.warn("{} : {}", LocalDateTime.now(), e.getMessage());

        FailDto failDto = FailDto.from(e, req.getRequestURI());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(failDto);
    }

    @ExceptionHandler
    public ResponseEntity<FailDto> handleException(Exception e, HttpServletRequest req) {
        log.error("{} : {}", LocalDateTime.now(), e.getMessage());

        FailDto failDto = new FailDto(CommonErrorCode.INTERNAL_SERVER_ERROR, "Internal Server Error", req.getRequestURI(), null);
        return ResponseEntity
                .status(CommonErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(failDto);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("{} : {}", LocalDateTime.now(), ex.getMessage());

        ServletWebRequest servletWebRequest = (ServletWebRequest)request;
        String path = servletWebRequest.getRequest().getRequestURI();

        Map<String, String> failedFields = getFailedFields(ex);
        FailDto failDto = new FailDto(CommonErrorCode.VALIDATION_FAILED, "Field validation failed", path, Map.of("fields", failedFields));
        return ResponseEntity
                .status(CommonErrorCode.VALIDATION_FAILED.getStatus())
                .body(failDto);
    }

    private Map<String, String> getFailedFields(MethodArgumentNotValidException ex) {
        Map<String, String> failedFields = new HashMap<>();
        BindingResult bindingResult = ex.getBindingResult();

        bindingResult.getFieldErrors().forEach(error -> {
            failedFields.put(error.getField(), error.getDefaultMessage());
        });
        return failedFields;
    }
}

