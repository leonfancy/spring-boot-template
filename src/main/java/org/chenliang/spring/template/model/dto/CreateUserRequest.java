package org.chenliang.spring.template.model.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class CreateUserRequest {
  @NotBlank
  private String name;
  @NotBlank
  @Email
  private String email;
  @NotBlank
  private String password;
}
