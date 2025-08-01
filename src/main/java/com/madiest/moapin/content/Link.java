package com.madiest.moapin.content;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * Link content, stores an external URL.
 */
@Setter
@Getter
@Entity
@DiscriminatorValue("LINK")
public class Link extends Content {

    @Column(name = "url", nullable = false)
    private String url;

}