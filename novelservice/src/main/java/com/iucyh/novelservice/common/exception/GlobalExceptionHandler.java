package com.iucyh.novelservice.common.exception;

import com.iucyh.novelservice.dto.FailDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<FailDto> handleServiceException(ServiceException ex, HttpServletRequest req) {
        log.warn("{} : {}", LocalDateTime.now(), ex.getMessage());

        String path = req.getRequestURI();
        FailDto failDto = FailDto.from(ex, path);
        return ResponseEntity
                .status(ex.getErrorCode().getStatus())
                .body(failDto);
    }

    @ExceptionHandler
    public ResponseEntity<FailDto> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        log.warn("{} : {}", LocalDateTime.now(), ex.getMessage());

        ErrorCode errorCode = CommonErrorCode.METHOD_ARGUMENT_TYPE_MISMATCH;
        String path = req.getRequestURI();

        LinkedHashMap<String, Object> causes = new LinkedHashMap<>();
        String parameterName = ex.getName();
        String parameterType = ex.getRequiredType() == null ? "" : ex.getRequiredType().getSimpleName();

        causes.put("parameterName", parameterName);
        causes.put("requiredType", parameterType);

        FailDto failDto = new FailDto(errorCode, errorCode.getDefaultMessage(), path, causes);
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(failDto);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.warn("{} : {}", LocalDateTime.now(), ex.getMessage());

        ErrorCode errorCode = CommonErrorCode.MISSING_PATH_VARIABLE;
        String path = getRequestPath(request);

        String variableName = ex.getVariableName();
        Map<String, Object> causes = Map.of("missingVariable", variableName);

        FailDto failDto = new FailDto(errorCode, errorCode.getDefaultMessage(), path, causes);
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(failDto);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.warn("{} : {}", LocalDateTime.now(), ex.getMessage());

        ErrorCode errorCode = CommonErrorCode.HTTP_MESSAGE_NOT_READABLE;
        String path = getRequestPath(request);

        FailDto failDto = new FailDto(errorCode, errorCode.getDefaultMessage(), path);
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(failDto);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("{} : {}", LocalDateTime.now(), ex.getMessage());

        ErrorCode errorCode = CommonErrorCode.VALIDATION_FAILED;
        String path = getRequestPath(request);
        List<LinkedHashMap<String, String>> failedFields = getFailedFields(ex);

        FailDto failDto = new FailDto(errorCode, errorCode.getDefaultMessage(), path, Map.of("fields", failedFields));
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(failDto);
    }

    @ExceptionHandler
    public ResponseEntity<FailDto> handleException(Exception ex, HttpServletRequest req) {
        log.error("{} : {}, stack trace : {}", LocalDateTime.now(), ex.getMessage(), ex.getStackTrace());

        ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
        String path = req.getRequestURI();

        FailDto failDto = new FailDto(errorCode, errorCode.getDefaultMessage(), path);
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(failDto);
    }

    private List<LinkedHashMap<String, String>> getFailedFields(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        return bindingResult.getFieldErrors().stream()
                .map((error) -> {
                    String message = error.getDefaultMessage() == null ? "" : error.getDefaultMessage();

                    LinkedHashMap<String, String> result = new LinkedHashMap<>();
                    result.put("field", error.getField());
                    result.put("message", message);
                    return result;
                })
                .toList();
    }

    private String getRequestPath(WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest)request;
        return servletWebRequest.getRequest().getRequestURI();
    }
}

