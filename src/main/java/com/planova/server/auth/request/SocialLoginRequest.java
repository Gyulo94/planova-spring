package com.planova.server.auth.request;

import com.planova.server.user.entity.Provider;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Schema(title = "AUTH_REQ_03 : 소셜 로그인 요청 DTO")
public class SocialLoginRequest {

  @NotBlank(message = "이름은 필수 입력 항목입니다.")
  @Schema(description = "사용자 이름", example = "John Doe")
  private String name;

  @NotBlank(message = "이메일은 필수 입력 항목입니다.")
  @Email(message = "유효한 이메일 형식이 아닙니다.")
  @Schema(description = "사용자 이메일", example = "john.doe@example.com")
  private String email;

  @Schema(description = "사용자 비밀번호", example = "password123")
  private String password;

  @Schema(description = "사용자 프로필 이미지 URL", example = "https://example.com/profile.jpg")
  private String image;

  @Schema(description = "소셜 로그인 제공자", example = "구글 || 카카오")
  private Provider provider;
}
