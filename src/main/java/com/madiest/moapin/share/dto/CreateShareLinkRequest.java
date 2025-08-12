package com.madiest.moapin.share.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class CreateShareLinkRequest {
    @NotNull
    private Long contentId;
    private Instant expiresAt;
    private Integer maxDownloads;
}