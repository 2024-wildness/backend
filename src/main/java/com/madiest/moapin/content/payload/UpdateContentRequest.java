package com.madiest.moapin.content.payload;

import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO for updating an existing content item.
 */
@Setter
@Getter
public class UpdateContentRequest {

    private Long categoryId;

    private String fileKey;

    private String url;

    private String textContent;

}