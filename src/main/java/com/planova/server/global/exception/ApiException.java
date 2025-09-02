package com.planova.server.global.exception;

import com.planova.server.global.error.ErrorCodeInterface;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException implements ApiExceptionInterface {

  private final ErrorCodeInterface errorCodeInterface;

  private final String errorMessage;

  public ApiException(ErrorCodeInterface errorCodeInterface) {
    super(errorCodeInterface.getMessage());
    this.errorCodeInterface = errorCodeInterface;
    this.errorMessage = errorCodeInterface.getMessage();
  }

  public ApiException(ErrorCodeInterface errorCodeInterface, String errorMessage) {
    super(errorMessage);
    this.errorCodeInterface = errorCodeInterface;
    this.errorMessage = errorMessage;
  }

  public ApiException(ErrorCodeInterface errorCodeInterface, Throwable tx) {
    super(tx);
    this.errorCodeInterface = errorCodeInterface;
    this.errorMessage = errorCodeInterface.getMessage();
  }

  public ApiException(ErrorCodeInterface errorCodeInterface, Throwable tx, String errorMessage) {
    super(tx);
    this.errorCodeInterface = errorCodeInterface;
    this.errorMessage = errorMessage;
  }
}