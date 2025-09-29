package com.planova.server.global.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseMessage implements ResponseMessageInterface {
  SEND_EMAIL_SUCCESS("이메일을 성공적으로 보냈습니다. 링크를 통해 진행해주세요."),
  RESET_PASSWORD_SUCCESS("비밀번호가 성공적으로 변경되었습니다. 다시 로그인을 해주세요."),
  LOGOUT_SUCCESS("로그아웃 되었습니다. 다음에 또 만나요 !"),

  CREATE_WORKSPACE_SUCCESS("워크스페이스가 성공적으로 생성되었습니다."),
  UPDATE_WORKSPACE_SUCCESS("워크스페이스가 성공적으로 수정되었습니다."),
  DELETE_WORKSPACE_SUCCESS("워크스페이스가 성공적으로 삭제되었습니다."),
  RESET_INVITE_CODE_SUCCESS("워크스페이스 초대 코드가 성공적으로 재발급되었습니다."),

  JOIN_WORKSPACE_SUCCESS("워크스페이스에 성공적으로 참가하였습니다."),

  UPDATE_WORKSPACE_MEMBER_SUCCESS("워크스페이스 멤버 권한이 성공적으로 변경되었습니다."),
  REMOVE_WORKSPACE_MEMBER_SUCCESS("워크스페이스 멤버가 성공적으로 추방되었습니다."),
  CREATE_PROJECT_SUCCESS("프로젝트가 성공적으로 생성되었습니다."),
  UPDATE_PROJECT_SUCCESS("프로젝트가 성공적으로 수정되었습니다."),
  DELETE_PROJECT_SUCCESS("프로젝트가 성공적으로 삭제되었습니다."),

  CREATE_TASK_SUCCESS("작업이 성공적으로 생성되었습니다."),
  ;

  private final String message;
}
