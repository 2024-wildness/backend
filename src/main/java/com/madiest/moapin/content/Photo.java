package com.madiest.moapin.content;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
/**
 * Photo content, stores a reference to the file in MinIO.
 */
@Entity
@DiscriminatorValue("PHOTO")
public class Photo extends Content {

    @Column(name = "file_key", nullable = false)
    private String fileKey;

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }
}