package com.madiest.moapin.share;

import com.madiest.moapin.content.Content;
import com.madiest.moapin.content.ContentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ShareLinkServiceTest {

    @Test
    void testCreateAndAccess() {
        ShareLinkRepository repo = Mockito.mock(ShareLinkRepository.class);
        ContentRepository contentRepo = Mockito.mock(ContentRepository.class);
        ShareLinkService service = new ShareLinkService(repo, contentRepo);
        Authentication auth = Mockito.mock(Authentication.class);
        Mockito.when(auth.getName()).thenReturn("user");
        Content content = new Content() {};
        Mockito.when(contentRepo.findById(1L)).thenReturn(Optional.of(content));

        Mockito.when(repo.save(Mockito.any())).thenAnswer(a -> a.getArguments()[0]);
        ShareLink link = service.createShareLink(1L, Instant.now().plusSeconds(60), 1, auth);
        assertThat(link.getToken()).isNotNull();

        Mockito.when(repo.findByToken(link.getToken())).thenReturn(Optional.of(link));
        Content accessed = service.accessShareLink(link.getToken());
        assertThat(accessed).isSameAs(content);

        assertThat(link.getDownloadCount()).isEqualTo(1);
        assertThatThrownBy(() -> service.accessShareLink(link.getToken()))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Download limit");
    }

    @Test
    void testExpired() {
        ShareLinkRepository repo = Mockito.mock(ShareLinkRepository.class);
        ContentRepository contentRepo = Mockito.mock(ContentRepository.class);
        ShareLinkService service = new ShareLinkService(repo, contentRepo);
        ShareLink link = new ShareLink();
        link.setToken("t");
        link.setExpiresAt(Instant.now().minusSeconds(1));
        Mockito.when(repo.findByToken("t")).thenReturn(Optional.of(link));

        assertThatThrownBy(() -> service.accessShareLink("t"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("expired");
    }
}
