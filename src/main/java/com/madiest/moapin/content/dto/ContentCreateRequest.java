package com.madiest.moapin.content.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ContentCreateRequest {
  private Long categoryId;
  private String type;
  private String title;
  private String body;
  private String url;
  private String fileKey;
}
