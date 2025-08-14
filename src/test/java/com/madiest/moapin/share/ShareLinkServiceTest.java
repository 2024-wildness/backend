package com.madiest.moapin.share;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.madiest.moapin.auth.model.User;
import com.madiest.moapin.content.model.Content;
import com.madiest.moapin.content.repository.ContentRepository;
import com.madiest.moapin.share.model.ShareLink;
import com.madiest.moapin.share.repository.ShareLinkRepository;
import com.madiest.moapin.share.service.ShareLinkService;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

public class ShareLinkServiceTest {

  @Test
  void testCreateAndAccess() {
    ShareLinkRepository repo = Mockito.mock(ShareLinkRepository.class);
    ContentRepository contentRepo = Mockito.mock(ContentRepository.class);
    ShareLinkService service = new ShareLinkService(repo, contentRepo);
    User user = new User();
    user.setUsername("user");
    user.setEmail("user@example.com");
    user.setPassword("password");
    Content content = new Content("Test Content", user, null) {};
    Mockito.when(contentRepo.findById(1L)).thenReturn(Optional.of(content));

    Mockito.when(repo.save(Mockito.any())).thenAnswer(a -> a.getArguments()[0]);
    ShareLink link = service.createShareLink(1L, Instant.now().plusSeconds(60), 1);
    assertThat(link.getToken()).isNotNull();

    Mockito.when(repo.findByToken(link.getToken())).thenReturn(Optional.of(link));
    Content accessed = service.accessShareLink(link.getToken());
    assertThat(accessed).isSameAs(content);

    assertThat(link.getDownloadCount()).isEqualTo(1);
    assertThatThrownBy(() -> service.accessShareLink(link.getToken()))
        .isInstanceOf(ResponseStatusException.class)
        .hasMessageContaining("Download limit reached");
  }

  @Test
  void testExpired() {
    ShareLinkRepository repo = Mockito.mock(ShareLinkRepository.class);
    ContentRepository contentRepo = Mockito.mock(ContentRepository.class);
    ShareLinkService service = new ShareLinkService(repo, contentRepo);
    User user = new User();
    user.setUsername("user");
    user.setEmail("user@example.com");
    user.setPassword("password");
    Content content = new Content("Test Content", user, null) {};
    ShareLink link = new ShareLink(content, Instant.now().minusSeconds(1), null);
    Mockito.when(repo.findByToken("t")).thenReturn(Optional.of(link));

    assertThatThrownBy(() -> service.accessShareLink("t"))
        .isInstanceOf(ResponseStatusException.class)
        .hasMessageContaining("Share link has expired");
  }
}
