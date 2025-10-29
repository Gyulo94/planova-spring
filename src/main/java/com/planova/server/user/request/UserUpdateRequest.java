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
@Schema(title = "USER_REQ_02 : 사용자 정보 수정 요청 DTO")
public class UserUpdateRequest {

  @Schema(description = "사용자 이름", example = "John Doe")
  private String name;

  @Schema(description = "사용자 프로필 이미지 URL", example = "https://example.com/profile.jpg")
  private String image;
}
