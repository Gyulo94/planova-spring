package com.planova.server.global.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.planova.server.global.api.Api;
import com.planova.server.global.error.ErrorCode;
import com.planova.server.global.error.ErrorCodeInterface;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

        Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandler.class);

        @ExceptionHandler(value = ApiException.class)
        public ResponseEntity<Api<Object>> apiException(
                        ApiException e, HttpServletRequest request) {
                LOGGER.error("ApiException 발생: [에러 코드: {}][메시지: {}]",
                                e.getErrorCodeInterface().getHttpStatusCode(), e.getMessage(), e);

                ErrorCodeInterface errorCode = e.getErrorCodeInterface();
                // String requestPath = request.getRequestURI();

                Api<Object> errorResponse = Api.ERROR(
                                e.getErrorCodeInterface(),
                                e.getErrorMessage());

                return ResponseEntity
                                .status(errorCode.getHttpStatusCode())
                                .body(errorResponse);
        }

        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<Api<Object>> handleRuntimeException(
                        RuntimeException e,
                        HttpServletRequest request) {
                LOGGER.error("예상치 못한 런타임 예외 발생: {}", e.getMessage(), e);

                // String requestPath = request.getRequestURI();

                Api<Object> errorResponse = Api.ERROR(
                                ErrorCode.SERVER_ERROR,
                                "예상치 못한 오류가 발생했습니다.");

                return ResponseEntity
                                .status(ErrorCode.SERVER_ERROR.getHttpStatusCode())
                                .body(errorResponse);
        }
}