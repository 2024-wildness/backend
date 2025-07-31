package com.madiest.moapin.content;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Link content, stores an external URL.
 */
@Entity
@DiscriminatorValue("LINK")
public class Link extends Content {

    @Column(name = "url", nullable = false)
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}