package com.madiest.moapin.content;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests verifying polymorphic Content JPA mappings.
 */
@DataJpaTest
class ContentEntityTest {

    @Autowired
    private ContentRepository contentRepository;

    @Test
    void persistAndRetrieveSubtypes() {
        Photo photo = new Photo();
        photo.setFileKey("file123");
        Photo savedPhoto = contentRepository.save(photo);
        assertThat(contentRepository.findById(savedPhoto.getId()).get()).isInstanceOf(Photo.class);

        Link link = new Link();
        link.setUrl("https://example.com");
        Link savedLink = contentRepository.save(link);
        assertThat(contentRepository.findById(savedLink.getId()).get()).isInstanceOf(Link.class);

        Note note = new Note();
        note.setTextContent("Hello World");
        Note savedNote = contentRepository.save(note);
        assertThat(contentRepository.findById(savedNote.getId()).get()).isInstanceOf(Note.class);
    }
}