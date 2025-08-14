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
@DiscriminatorValue("NOTE")
public class Note extends Content {

  private String body;

  public Note(String title, User user, Category category, String body) {
    super(title, user, category);
    this.body = body;
  }

  public void setBody(String body) {
    this.body = body;
  }
}
