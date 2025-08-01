package com.madiest.moapin.content.payload;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO for completing a photo upload and creating the Photo entity.
 */
@Setter
@Getter
public class PhotoCompleteRequest {

    @NotBlank
    private String key;

    private Long categoryId;

}