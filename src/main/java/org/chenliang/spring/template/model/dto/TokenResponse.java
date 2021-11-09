package org.chenliang.spring.template.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenResponse {
  private String accessToken;
  private Integer expireIn;
}
