package com.planova.server.user.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.planova.server.global.api.Api;
import com.planova.server.global.message.ResponseMessage;
import com.planova.server.user.request.UserPasswordRequest;
import com.planova.server.user.request.UserUpdateRequest;
import com.planova.server.user.response.UserResponse;
import com.planova.server.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
@Tag(name = "사용자", description = "사용자 관련 API")
public class UserController {

  private final UserService userService;

  @GetMapping("{id}")
  @Operation(summary = "사용자 조회", description = "특정 ID를 가진 사용자의 정보를 조회합니다.")
  public Api<UserResponse> findUserById(@PathVariable("id") UUID id) {
    UserResponse response = userService.findUserById(id);
    return Api.OK(response);
  }

  @PutMapping("{id}")
  @Operation(summary = "사용자 정보 수정", description = "특정 ID를 가진 사용자의 정보를 수정합니다.")
  public Api<UserResponse> updateUser(@PathVariable("id") UUID id, @RequestBody UserUpdateRequest request) {
    UserResponse response = userService.updateUser(id, request);
    return Api.OK(response, ResponseMessage.UPDATE_USER_SUCCESS);
  }

  @PutMapping("{id}/change-password")
  @Operation(summary = "사용자 비밀번호 변경", description = "특정 ID를 가진 사용자의 비밀번호를 변경합니다.")
  public Api<UserResponse> updateUserPassword(@PathVariable("id") UUID id, @RequestBody UserPasswordRequest request) {
    UserResponse response = userService.updateUserPassword(id, request);
    return Api.OK(response, ResponseMessage.UPDATE_USER_PASSWORD_SUCCESS);
  }

  @DeleteMapping("{id}")
  @Operation(summary = "사용자 탈퇴", description = "특정 ID를 가진 사용자를 삭제합니다.")
  public Api<Void> deleteUser(@PathVariable("id") UUID id) {
    userService.deleteUser(id);
    return Api.OK(ResponseMessage.DELETE_USER_SUCCESS);
  }
}
