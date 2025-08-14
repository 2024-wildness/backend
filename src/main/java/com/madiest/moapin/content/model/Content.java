package com.madiest.moapin.content.model;

import com.madiest.moapin.auth.model.User;
import com.madiest.moapin.category.model.Category;
import com.madiest.moapin.persistence.Auditable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "content_type")
public abstract class Content extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private Category category;

  private boolean isPinned;

  private int viewCount;

  public Content(String title, User user, Category category) {
    this.title = title;
    this.user = user;
    this.category = category;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
