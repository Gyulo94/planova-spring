package com.planova.server.user.request;

import com.planova.server.user.entity.Provider;

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
@Schema(title = "USER_REQ_01 : 회원가입 요청 DTO")
public class UserRequest {

  @NotBlank(message = "이름은 필수 입력 항목입니다.")
  @Schema(description = "사용자 이름", example = "John Doe")
  private String name;

  @NotBlank(message = "이메일은 필수 입력 항목입니다.")
  @Email(message = "유효한 이메일 형식이 아닙니다.")
  @Schema(description = "사용자 이메일", example = "john.doe@example.com")
  private String email;

  @NotBlank(message = "이메일 인증 토큰이 필요합니다.")
  @Schema(description = "이메일 인증 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
  private String token;

  @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
  @Schema(description = "사용자 비밀번호", example = "password123")
  private String password;

  @Schema(description = "사용자 프로필 이미지 URL", example = "https://example.com/profile.jpg")
  private String image;

  @Schema(description = "계정 유형", example = "이메일 || 구글 || 카카오")
  private Provider provider;

}