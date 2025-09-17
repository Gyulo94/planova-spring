package com.planova.server.global.error;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode implements ErrorCodeInterface {
  OK(HttpStatus.OK.value(), "성공"),
  BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "잘못된 요청입니다."),
  SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 에러가 발생했습니다."),
  NULL_POINT(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Null point 에러"),
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "인증이 필요합니다."),
  FORBIDDEN(HttpStatus.FORBIDDEN.value(), "접근이 거부되었습니다."),

  DUPLICATED_USERNAME(HttpStatus.CONFLICT.value(), "사용중인 유저아이디입니다."),
  DUPLICATED_EMAIL(HttpStatus.CONFLICT.value(), "이미 사용중인 이메일입니다."),
  FAILD_TO_SIGN_UP(HttpStatus.INTERNAL_SERVER_ERROR.value(), "회원가입에 실패했습니다."),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 유저입니다."),
  FAILED_TO_LOGIN(HttpStatus.UNAUTHORIZED.value(), "유저아이디 혹은 비밀번호가 맞지 않습니다."),
  FORBIDDEN_RESET_PASSWORD_SOCIAL_USER(HttpStatus.FORBIDDEN.value(), "소셜 로그인 유저는 비밀번호 재설정이 불가능합니다."),
  SAME_PASSWORD(HttpStatus.BAD_REQUEST.value(), "기존 비밀번호와 동일합니다."),

  ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED.value(), "액세스 토큰이 만료되었습니다."),
  INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 토큰입니다."),
  REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED.value(), "리프레시 토큰이 없습니다."),
  INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 리프레시 토큰입니다."),
  MISSING_USER_ID(HttpStatus.UNAUTHORIZED.value(), "사용자 ID가 없습니다."),
  REFRESH_TOKEN_CLAIMS_NOT_FOUND(HttpStatus.UNAUTHORIZED.value(), "리프레시 토큰 클레임이 없습니다."),
  INVALID_USER_ID_FORMAT(HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 사용자 ID 형식입니다."),

  CREATE_WORKSPACE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "워크스페이스 생성에 실패했습니다."),

  WORKSPACE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 워크스페이스입니다."),
  MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "워크스페이스 멤버가 아닙니다."),

  SAVE_IMAGE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "이미지 저장에 실패했습니다."),
  ;

  private final Integer httpStatusCode;
  private final String message;
}
