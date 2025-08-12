package com.madiest.moapin.content.model;

import com.madiest.moapin.auth.model.User;
import com.madiest.moapin.category.model.Category;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("LINK")
public class Link extends Content {

    private String url;

    public Link(String title, User user, Category category, String url) {
        super(title, user, category);
        this.url = url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
