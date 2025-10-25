package com.iucyh.novelservice.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iucyh.novelservice.common.exception.testsupport.TestBodyDto;
import com.iucyh.novelservice.common.exception.testsupport.TestController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(TestController.class)
@WithMockUser
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("PathVariable 에 잘못된 타입을 전달하면 Method Argument Type Mismatch 예외가 발생한다")
    void methodArgumentTypeMismatchHandleSuccess() throws Exception {
        // when
        // PathVariable 기대값은 Integer
        ResultActions action = mockMvc.perform(
                get("/test-con/path-variable/{variable}", "string")
        );
        
        // then
        action
                .andExpectAll(
                        getCommonResultMatchers(CommonErrorCode.METHOD_ARGUMENT_TYPE_MISMATCH)
                );
    }

    @Test
    @DisplayName("필수 PathVariable 없이 요청하면 No Resource Found 예외가 발생한다")
    void noResourceFoundHandleSuccess() throws Exception {
        // when
        ResultActions action = mockMvc.perform(
                get("/test-con/path-variable")
        );

        // then
        action
                .andExpectAll(
                        getCommonResultMatchers(CommonErrorCode.NO_RESOURCE_FOUND)
                );
    }

    @Test
    @DisplayName("필수 Request Param 없이 요청하면 Missing Servlet Request Parameter 예외가 발생한다")
    void missingServletRequestParameterHandleSuccess() throws Exception {
        // when
        ResultActions action = mockMvc.perform(
                get("/test-con/request-parameter")
        );

        // then
        action
                .andExpectAll(
                        getCommonResultMatchers(CommonErrorCode.MISSING_SERVLET_REQUEST_PARAMETER)
                );
    }

    @Test
    @DisplayName("Request body 바인딩이 실패하면 Http Message Not Readable 예외가 발생한다")
    void httpMessageNotReadableHandleSuccess() throws Exception {
        // when
        ResultActions action = mockMvc.perform(
                post("/test-con/request-body")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"test\", \"age\": \"string\"}")
                        .with(csrf())
        );

        // then
        action
                .andExpectAll(
                        getCommonResultMatchers(CommonErrorCode.HTTP_MESSAGE_NOT_READABLE)
                );
    }

    @Test
    @DisplayName("DTO validation 이 실패하면 Method Argument Not Valid 예외가 발생한다")
    void methodArgumentNotValidHandleSuccess() throws Exception {
        // when
        // dto의 name 필드는 최대 10자 까지만 허용
        TestBodyDto dto = new TestBodyDto("a".repeat(11), 10);
        ResultActions action = mockMvc.perform(
                post("/test-con/request-body")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf())
        );

        // then
        action
                .andExpectAll(
                        getCommonResultMatchers(CommonErrorCode.VALIDATION_FAILED)
                );
    }

    private ResultMatcher[] getCommonResultMatchers(ErrorCode errorCode) {
        return new ResultMatcher[] {
                jsonPath("$.isSuccess").value(false),
                jsonPath("$.code").value(errorCode.getCode()),
                jsonPath("$.status").value(errorCode.getStatus().value())
        };
    }
}
