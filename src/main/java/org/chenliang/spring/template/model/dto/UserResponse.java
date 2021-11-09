package org.chenliang.spring.template.model.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class UserResponse {
  private Integer id;
  private String name;
  private String email;
  private String role;
  private OffsetDateTime createdAt;
}
