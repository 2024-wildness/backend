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
@DiscriminatorValue("PHOTO")
public class Photo extends Content {

    private String fileKey;

    public Photo(String title, User user, Category category, String fileKey) {
        super(title, user, category);
        this.fileKey = fileKey;
    }
}
