package org.chenliang.spring.template.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
@Data
public class ErrorResponse {
  private Integer code;
  private String message;
  private Map<String, String> details;
}
