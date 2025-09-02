package com.planova.server.global.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseMessage implements ResponseMessageInterface {
  SEND_EMAIL_SUCCESS("이메일을 성공적으로 보냈습니다. 링크를 통해 진행해주세요."),
  RESET_PASSWORD_SUCCESS("비밀번호가 성공적으로 변경되었습니다. 다시 로그인을 해주세요."),
  LOGOUT_SUCCESS("로그아웃 되었습니다. 다음에 또 만나요 !"),
  ;

  private final String message;
}
