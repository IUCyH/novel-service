package com.iucyh.novelservice.common.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iucyh.novelservice.common.util.IpUtil;
import com.iucyh.novelservice.dto.FailDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String LOG_FORMAT = "REQUEST: {} {} | IP: {} \n MESSAGE: {} CAUSE: {}";
    private static final String LOG_LEVEL_INFO = "info";
    private static final String LOG_LEVEL_WARN = "warn";
    private static final String LOG_LEVEL_ERROR = "error";

    private final ObjectMapper objectMapper;

    @ExceptionHandler
    public ResponseEntity<FailDto> handleServiceException(ServiceException ex, HttpServletRequest req) {
        String path = req.getRequestURI();
        FailDto failDto = FailDto.from(ex, path);

        log(LOG_LEVEL_WARN, req, ex, ex.getCauses());
        return ResponseEntity
                .status(ex.getErrorCode().getStatus())
                .body(failDto);
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        HttpServletRequest req = getRequest(request);
        ErrorCode errorCode = CommonErrorCode.NO_RESOURCE_FOUND;

        String path = req.getRequestURI();
        FailDto failDto = new FailDto(errorCode, errorCode.getDefaultMessage(), path);

        log(LOG_LEVEL_INFO, req, ex, null);
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(failDto);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        HttpServletRequest req = getRequest(request);
        ErrorCode errorCode = CommonErrorCode.MISSING_SERVLET_REQUEST_PARAMETER;

        String parameterName = ex.getParameterName();
        String parameterType = ex.getParameterType();

        LinkedHashMap<String, Object> causes = new LinkedHashMap<>();
        causes.put("parameterName", parameterName);
        causes.put("parameterType", parameterType);

        String path = req.getRequestURI();
        FailDto failDto = new FailDto(errorCode, errorCode.getDefaultMessage(), path, causes);

        log(LOG_LEVEL_INFO, req, ex, causes);
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(failDto);
    }

    @ExceptionHandler
    public ResponseEntity<FailDto> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        ErrorCode errorCode = CommonErrorCode.METHOD_ARGUMENT_TYPE_MISMATCH;

        LinkedHashMap<String, Object> causes = new LinkedHashMap<>();
        String parameterName = ex.getName();
        String parameterType = ex.getRequiredType() == null ? "" : ex.getRequiredType().getSimpleName();

        causes.put("parameterName", parameterName);
        causes.put("requiredType", parameterType);

        String path = req.getRequestURI();
        FailDto failDto = new FailDto(errorCode, errorCode.getDefaultMessage(), path, causes);

        log(LOG_LEVEL_INFO, req, ex, causes);
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(failDto);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        HttpServletRequest req = getRequest(request);
        ErrorCode errorCode = CommonErrorCode.MISSING_PATH_VARIABLE;

        String variableName = ex.getVariableName();
        Map<String, Object> causes = Map.of("missingVariable", variableName);

        String path = req.getRequestURI();
        FailDto failDto = new FailDto(errorCode, errorCode.getDefaultMessage(), path, causes);

        log(LOG_LEVEL_WARN, req, ex, causes);
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(failDto);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        HttpServletRequest req = getRequest(request);
        ErrorCode errorCode = CommonErrorCode.HTTP_MESSAGE_NOT_READABLE;

        String path = req.getRequestURI();
        FailDto failDto = new FailDto(errorCode, errorCode.getDefaultMessage(), path);

        log(LOG_LEVEL_WARN, req, ex, null); // TODO: 원인이 된 Request Body 로깅
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(failDto);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        HttpServletRequest req = getRequest(request);
        ErrorCode errorCode = CommonErrorCode.VALIDATION_FAILED;

        List<LinkedHashMap<String, String>> failedFields = getFailedFields(ex);
        Map<String, Object> fieldErrors = Map.of("fields", failedFields);

        String path = req.getRequestURI();
        FailDto failDto = new FailDto(errorCode, errorCode.getDefaultMessage(), path, fieldErrors);

        log(LOG_LEVEL_INFO, req, ex, fieldErrors);
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(failDto);
    }

    @ExceptionHandler
    public ResponseEntity<FailDto> handleException(Exception ex, HttpServletRequest req) {
        ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;

        String path = req.getRequestURI();
        FailDto failDto = new FailDto(errorCode, errorCode.getDefaultMessage(), path);

        log(LOG_LEVEL_ERROR, req, ex, null);
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(failDto);
    }

    private void log(String level, HttpServletRequest request, Exception ex, Map<String, Object> causes) {
        String ip = IpUtil.getIpAddr(request);
        String causeMessage = "{}";
        if (causes != null) {
            try {
                causeMessage = objectMapper.writeValueAsString(causes);
            } catch (JsonProcessingException e) {
                log.error("Failed to serialize causes to json string", e);
            }
        }

        switch (level) {
            case LOG_LEVEL_INFO:
                log.info(LOG_FORMAT, request.getMethod(), request.getRequestURI(), ip, ex.getMessage(), causeMessage);
                break;
            case LOG_LEVEL_WARN:
                log.warn(LOG_FORMAT, request.getMethod(), request.getRequestURI(), ip, ex.getMessage(), causeMessage);
                break;
            case LOG_LEVEL_ERROR:
                log.error(LOG_FORMAT, request.getMethod(), request.getRequestURI(), ip, ex.getMessage(), causeMessage, ex);
                break;
        }
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

    private HttpServletRequest getRequest(WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest)request;
        return servletWebRequest.getRequest();
    }
}

