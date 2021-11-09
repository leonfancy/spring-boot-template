package org.chenliang.spring.template.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ResetPasswordRequest {
  @NotBlank
  private String oldPassword;

  @NotBlank
  private String newPassword;
}
