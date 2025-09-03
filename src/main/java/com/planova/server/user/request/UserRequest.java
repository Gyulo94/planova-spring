package com.planova.server.user.request;

import com.planova.server.user.entity.Provider;

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
public class UserRequest {

  @NotBlank(message = "이름은 필수 입력 항목입니다.")
  private String name;

  @NotBlank(message = "이메일은 필수 입력 항목입니다.")
  @Email(message = "유효한 이메일 형식이 아닙니다.")
  private String email;

  @NotBlank(message = "이메일 인증 토큰이 필요합니다.")
  private String token;

  @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
  private String password;

  private String image;

  private Provider provider;
}
