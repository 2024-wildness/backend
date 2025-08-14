package com.madiest.moapin.content.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ContentUpdateRequest {
  private String title;
  private String body;
  private String url;
}
