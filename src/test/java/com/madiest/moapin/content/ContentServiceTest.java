package com.madiest.moapin.content;

import com.madiest.moapin.auth.model.User;
import com.madiest.moapin.category.model.Category;
import com.madiest.moapin.category.repository.CategoryRepository;
import com.madiest.moapin.content.model.Content;
import com.madiest.moapin.content.model.Note;
import com.madiest.moapin.content.repository.ContentRepository;
import com.madiest.moapin.content.service.ContentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ContentServiceTest {

    @InjectMocks
    private ContentService contentService;

    @Mock
    private ContentRepository contentRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private User user;
    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        category = new Category("Test Category", user);

        when(authentication.getPrincipal()).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(categoryRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(category));
    }

    @Test
    void createContent() {
        Note note = new Note("Test Note", user, category, "This is a test note.");
        when(contentRepository.save(any(Content.class))).thenReturn(note);

        Content createdContent = contentService.createContent(1L, "NOTE", "Test Note", "This is a test note.", null, null);

        assertEquals("Test Note", createdContent.getTitle());
    }
}
