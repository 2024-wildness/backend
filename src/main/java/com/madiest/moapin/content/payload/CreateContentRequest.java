package com.madiest.moapin.content.payload;

import com.madiest.moapin.content.ContentType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO for creating a content item.
 */
@Setter
@Getter
public class CreateContentRequest {

    @NotNull
    private ContentType type;

    private Long categoryId;

    private String fileKey;

    private String url;

    private String textContent;

}