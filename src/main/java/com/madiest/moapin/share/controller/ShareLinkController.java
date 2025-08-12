package com.madiest.moapin.share.controller;

import com.madiest.moapin.share.dto.ShareLinkCreateRequest;
import com.madiest.moapin.share.service.ShareLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/share")
@RequiredArgsConstructor
public class ShareLinkController {

    private final ShareLinkService shareLinkService;

    @PostMapping
    public ResponseEntity<Void> createShareLink(@RequestBody ShareLinkCreateRequest request) {
        // TODO: Implement share link creation
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{shareId}")
    public ResponseEntity<Void> deleteShareLink(@PathVariable String shareId) {
        // TODO: Implement share link deletion
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Void> getShareLinks() {
        // TODO: Implement get share links
        return ResponseEntity.ok().build();
    }
}
