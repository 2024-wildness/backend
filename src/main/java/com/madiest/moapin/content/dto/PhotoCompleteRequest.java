package com.madiest.moapin.content.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/** Request DTO for completing a photo upload and creating the Photo entity. */
@Setter
@Getter
public class PhotoCompleteRequest {

  @NotBlank private String key;

  @NotNull private Long categoryId;

  @NotBlank private String title;
}
