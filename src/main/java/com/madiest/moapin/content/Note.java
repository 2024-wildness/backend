package com.madiest.moapin.content;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Note content, stores free-text content.
 */
@Entity
@DiscriminatorValue("NOTE")
public class Note extends Content {

    @Column(name = "text_content", columnDefinition = "TEXT")
    private String textContent;

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }
}