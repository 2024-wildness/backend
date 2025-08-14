package com.madiest.moapin.share.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ShareLinkCreateRequest {
  private Long contentId;
  private Integer expireDays;
  private Integer maxDownloads;
}
