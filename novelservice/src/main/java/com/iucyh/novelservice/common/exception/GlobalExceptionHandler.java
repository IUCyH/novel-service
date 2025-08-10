package com.iucyh.novelservice.common.exception;

import com.iucyh.novelservice.dto.FailDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

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
}

