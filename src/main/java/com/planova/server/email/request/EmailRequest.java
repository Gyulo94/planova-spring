package com.planova.server.email.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "EMAIL_REQ_01 : 이메일 요청 DTO")
public class EmailRequest {

  @Schema(description = "사용자 이메일", example = "john.doe@example.com")
  private String email;

  @Schema(description = "이메일 타입", example = "signup || reset")
  private String type;
}
