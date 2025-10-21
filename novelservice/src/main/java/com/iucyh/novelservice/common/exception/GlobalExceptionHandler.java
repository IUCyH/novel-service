package com.iucyh.novelservice.common.exception;

import com.iucyh.novelservice.dto.FailDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
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
    public ResponseEntity<FailDto> handleException(Exception e, HttpServletRequest req) {
        log.error("{} : {}, stack trace : {}", LocalDateTime.now(), e.getMessage(), e.getStackTrace());

        FailDto failDto = new FailDto(CommonErrorCode.INTERNAL_SERVER_ERROR, "Internal Server Error", req.getRequestURI(), null);
        return ResponseEntity
                .status(CommonErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(failDto);
    }

    @ExceptionHandler
    public ResponseEntity<FailDto> handleServiceException(ServiceException e, HttpServletRequest req) {
        log.warn("{} : {}", LocalDateTime.now(), e.getMessage());

        FailDto failDto = FailDto.from(e, req.getRequestURI());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(failDto);
    }

    @ExceptionHandler
    public ResponseEntity<FailDto> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest req) {
        log.warn("{} : {}", LocalDateTime.now(), e.getMessage());

        String path = req.getRequestURI();
        String parameterName = e.getName();
        String parameterType = e.getRequiredType() == null ? "Not provided" : e.getRequiredType().getSimpleName();

        FailDto failDto = new FailDto(CommonErrorCode.METHOD_ARGUMENT_TYPE_MISMATCH, "Parameter type mismatch", path, Map.of("parameterName", parameterName, "requiredParameterType", parameterType));
        return ResponseEntity
                .status(CommonErrorCode.METHOD_ARGUMENT_TYPE_MISMATCH.getStatus())
                .body(failDto);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.warn("{} : {}", LocalDateTime.now(), ex.getMessage());

        String path = getRequestPath(request);
        String variableName = ex.getVariableName();

        FailDto failDto = new FailDto(CommonErrorCode.MISSING_PATH_VARIABLE, "Missing path variable", path, Map.of("missingVariable", variableName));
        return ResponseEntity
                .status(CommonErrorCode.MISSING_PATH_VARIABLE.getStatus())
                .body(failDto);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.warn("{} : {}", LocalDateTime.now(), ex.getMessage());

        String path = getRequestPath(request);
        FailDto failDto = new FailDto(CommonErrorCode.HTTP_MESSAGE_NOT_READABLE, "Cannot read request body", path, null);

        return ResponseEntity
                .status(CommonErrorCode.HTTP_MESSAGE_NOT_READABLE.getStatus())
                .body(failDto);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("{} : {}", LocalDateTime.now(), ex.getMessage());

        String path = getRequestPath(request);
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

    private String getRequestPath(WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest)request;
        return servletWebRequest.getRequest().getRequestURI();
    }
}

