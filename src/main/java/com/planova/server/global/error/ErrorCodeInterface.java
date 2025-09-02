package com.planova.server.global.error;

public interface ErrorCodeInterface {

  Integer getHttpStatusCode();

  String getMessage();
}