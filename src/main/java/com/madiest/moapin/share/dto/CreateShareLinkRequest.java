package com.madiest.moapin.share.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateShareLinkRequest {
  @NotNull private Long contentId;
  private Instant expiresAt;
  private Integer maxDownloads;
}
