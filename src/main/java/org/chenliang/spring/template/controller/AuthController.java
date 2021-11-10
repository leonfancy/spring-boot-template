package org.chenliang.spring.template.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.chenliang.spring.template.model.dto.CreateUserRequest;
import org.chenliang.spring.template.model.dto.LoginRequest;
import org.chenliang.spring.template.model.dto.TokenResponse;
import org.chenliang.spring.template.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Tag(name = "Auth", description = "Login and register API")
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/signup")
  public TokenResponse register(@Valid @RequestBody CreateUserRequest request) {
    return authService.register(request);
  }


  @PostMapping("/login")
  public TokenResponse login(@Valid @RequestBody LoginRequest request) {
    return authService.login(request);
  }
}
