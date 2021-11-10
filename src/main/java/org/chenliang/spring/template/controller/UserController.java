package org.chenliang.spring.template.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.chenliang.spring.template.model.dto.ResetPasswordRequest;
import org.chenliang.spring.template.model.dto.UpdateUserRequest;
import org.chenliang.spring.template.model.dto.UserResponse;
import org.chenliang.spring.template.model.entity.User;
import org.chenliang.spring.template.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Tag(name = "User", description = "API for current login user")
public class UserController {
  private final UserService userService;

  private final ModelMapper modelMapper;

  public UserController(UserService userService, ModelMapper modelMapper) {
    this.userService = userService;
    this.modelMapper = modelMapper;
  }

  @GetMapping("/user")
  public UserResponse getCurrentUser(@AuthenticationPrincipal User user) {
    return modelMapper.map(user, UserResponse.class);
  }

  @PutMapping("/user")
  public UserResponse updateCurrentUser(@AuthenticationPrincipal User user, @Valid @RequestBody UpdateUserRequest request) {
    User updatedUser = userService.updateCurrentUser(user, request);
    return modelMapper.map(updatedUser, UserResponse.class);
  }

  @PutMapping("/user/password")
  public void resetPassword(@Valid @RequestBody ResetPasswordRequest request, @AuthenticationPrincipal User user) {
    userService.resetPassword(request, user);
  }
}
