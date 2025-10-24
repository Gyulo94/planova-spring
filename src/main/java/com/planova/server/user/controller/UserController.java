package com.planova.server.user.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.planova.server.global.api.Api;
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
}
