package com.madiest.moapin.content;

import static org.assertj.core.api.Assertions.assertThat;

import com.madiest.moapin.auth.model.User;
import com.madiest.moapin.auth.repository.UserRepository;
import com.madiest.moapin.category.model.Category;
import com.madiest.moapin.category.repository.CategoryRepository;
import com.madiest.moapin.content.model.Link;
import com.madiest.moapin.content.model.Note;
import com.madiest.moapin.content.model.Photo;
import com.madiest.moapin.content.repository.ContentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@ActiveProfiles("test")
class ContentEntityTest {

  @Autowired private ContentRepository contentRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private CategoryRepository categoryRepository;

  private User user;
  private Category category;

  @BeforeEach
  void setUp() {
    user = new User();
    user.setUsername("testuser");
    user.setEmail("test@example.com");
    user.setPassword("password");
    userRepository.save(user);

    category = new Category("Test Category", user);
    categoryRepository.save(category);
  }

  @Test
  @Transactional
  void persistAndRetrieveSubtypes() {
    Photo photo = new Photo("Test Photo", user, category, "file123");
    Photo savedPhoto = contentRepository.save(photo);
    assertThat(contentRepository.findById(savedPhoto.getId()).get()).isInstanceOf(Photo.class);

    Link link = new Link("Test Link", user, category, "https://example.com");
    Link savedLink = contentRepository.save(link);
    assertThat(contentRepository.findById(savedLink.getId()).get()).isInstanceOf(Link.class);

    Note note = new Note("Test Note", user, category, "Hello World");
    Note savedNote = contentRepository.save(note);
    assertThat(contentRepository.findById(savedNote.getId()).get()).isInstanceOf(Note.class);
  }
}
