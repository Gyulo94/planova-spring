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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("{id}")
  public Api<UserResponse> findUserById(@PathVariable("id") UUID id) {
    UserResponse response = userService.findUserById(id);
    return Api.OK(response);
  }

  @PutMapping("{id}")
  public Api<UserResponse> updateUser(@PathVariable("id") UUID id, @RequestBody UserUpdateRequest request) {
    UserResponse response = userService.updateUser(id, request);
    return Api.OK(response, ResponseMessage.UPDATE_USER_SUCCESS);
  }

  @PutMapping("{id}/change-password")
  public Api<UserResponse> updateUserPassword(@PathVariable("id") UUID id, @RequestBody UserPasswordRequest request) {
    UserResponse response = userService.updateUserPassword(id, request);
    return Api.OK(response, ResponseMessage.UPDATE_USER_PASSWORD_SUCCESS);
  }

  @DeleteMapping("{id}")
  public Api<Void> deleteUser(@PathVariable("id") UUID id) {
    userService.deleteUser(id);
    return Api.OK(ResponseMessage.DELETE_USER_SUCCESS);
  }
}
