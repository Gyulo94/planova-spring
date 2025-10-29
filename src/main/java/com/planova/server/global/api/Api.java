package com.planova.server.global.api;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.planova.server.global.error.ErrorCode;
import com.planova.server.global.error.ErrorCodeInterface;
import com.planova.server.global.message.ResponseMessageInterface;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Api<T> {

  @Schema(description = "HTTP 상태 코드", example = "200")
  private int statusCode;

  @Schema(description = "응답 메시지", example = "요청이 성공적으로 처리되었습니다.")
  private String message;

  @Schema(description = "응답 생성 일시", example = "2023-10-05T14:48:00")
  private String timestamp;

  @Schema(description = "요청 HTTP 메서드", example = "GET")
  private String method;

  @Schema(description = "요청 경로", example = "/api/auth")
  private String path;

  @Schema(description = "응답 바디")
  private T body;

  @Builder(toBuilder = true)
  private Api(int statusCode, String message, String timestamp, String path, String method, T body) {
    this.statusCode = statusCode;
    this.message = message;
    this.timestamp = Objects.requireNonNullElse(timestamp, getCurrentTimestamp());
    this.path = Objects.requireNonNullElse(path, getCurrentRequestUri());
    this.method = Objects.requireNonNullElse(method, getCurrentMethod());
    this.body = body;
  }

  private static String getCurrentRequestUri() {
    try {
      ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
      return sra.getRequest().getRequestURI();
    } catch (IllegalStateException e) {
      return "N/A";
    }
  }

  private static String getCurrentMethod() {
    try {
      ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
      return sra.getRequest().getMethod();
    } catch (IllegalStateException e) {
      return "N/A";
    }
  }

  private static String getCurrentTimestamp() {
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }

  public static <T> Api<T> OK(T data) {
    return Api.<T>builder()
        .statusCode(200)
        .message(ErrorCode.OK.getMessage())
        .body(data)
        .build();
  }

  public static <T> Api<T> OK(T data, ResponseMessageInterface responseMessage) {
    return Api.<T>builder()
        .statusCode(ErrorCode.OK.getHttpStatusCode())
        .message(responseMessage.getMessage())
        .body(data)
        .build();
  }

  public static <T> Api<T> OK(ResponseMessageInterface responseMessage) {
    return Api.<T>builder()
        .statusCode(ErrorCode.OK.getHttpStatusCode())
        .message(responseMessage.getMessage())
        .body(null)
        .build();
  }

  // public static Api<Object> OK(String message) {
  // return Api.builder()
  // .statusCode(ErrorCode.OK.getHttpStatusCode())
  // .message(message)
  // .body(null)
  // .build();
  // }

  public static Api<Object> ERROR(ErrorCodeInterface errorCodeInterface, String message) {
    return Api.builder()
        .statusCode(errorCodeInterface.getHttpStatusCode())
        .message(message)
        .build();
  }

  public static Api<Object> ERROR(ErrorCodeInterface errorCodeInterface) {
    return Api.builder()
        .statusCode(errorCodeInterface.getHttpStatusCode())
        .message(errorCodeInterface.getMessage())
        .build();
  }

  public static Api<Object> ERROR(int statusCode, ResponseMessageInterface responseMessage) {
    return Api.builder()
        .statusCode(statusCode)
        .message(responseMessage.getMessage())
        .body(null)
        .build();
  }
}