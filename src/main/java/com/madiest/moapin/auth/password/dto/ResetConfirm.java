package com.madiest.moapin.auth.password.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public class ResetConfirm {
  @NotBlank @Setter @Getter private String token;
  @NotBlank @Setter @Getter private String password;
}
