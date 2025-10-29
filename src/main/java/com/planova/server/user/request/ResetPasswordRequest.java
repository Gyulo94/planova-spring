package com.planova.server.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
@Schema(title = "USER_REQ_04 : 비밀번호 재설정 요청 DTO")
public class ResetPasswordRequest {

  @NotBlank(message = "이메일은 필수 입력 항목입니다.")
  @Email(message = "유효한 이메일 형식이 아닙니다.")
  @Schema(description = "사용자 이메일", example = "john.doe@example.com")
  private String email;

  @NotBlank(message = "이메일 인증 토큰이 필요합니다.")
  @Schema(description = "이메일 인증 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
  private String token;

  @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
  @Schema(description = "새 비밀번호", example = "newPassword123")
  private String password;
}
