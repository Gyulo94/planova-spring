package com.planova.server.global.exception;

import com.planova.server.global.error.ErrorCodeInterface;

public interface ApiExceptionInterface {

  ErrorCodeInterface getErrorCodeInterface();

  String getErrorMessage();
}
