package com.planova.server.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "USER_REQ_03 : 사용자 비밀번호 변경 요청 DTO")
public class UserPasswordRequest {

  @Schema(description = "현재 비밀번호", example = "currentPassword123")
  private String currentPassword;

  @Schema(description = "새 비밀번호", example = "newPassword123")
  private String newPassword;
}
