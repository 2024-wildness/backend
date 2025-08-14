package com.madiest.moapin.auth.password.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public class ResetRequest {
  @NotBlank @Setter @Getter private String email;
}
