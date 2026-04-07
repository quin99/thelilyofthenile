package com.thelilyofthenile.backend.controller;

import com.thelilyofthenile.backend.model.SiteImage;
import com.thelilyofthenile.backend.repository.SiteImageRepository;
import com.thelilyofthenile.backend.service.ImageStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/site-images")
public class AdminSiteImageController {

    private final SiteImageRepository repo;
    private final ImageStorageService imageStorageService;

    public AdminSiteImageController(SiteImageRepository repo, ImageStorageService imageStorageService) {
        this.repo = repo;
        this.imageStorageService = imageStorageService;
    }

    @PutMapping(value = "/{slot}", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, String>> upload(
            @PathVariable String slot,
            @RequestPart("image") MultipartFile image
    ) throws IOException {
        String imageUrl = imageStorageService.store(image);
        SiteImage siteImage = repo.findBySlot(slot).orElse(new SiteImage());
        siteImage.setSlot(slot);
        siteImage.setImageUrl(imageUrl);
        repo.save(siteImage);
        return ResponseEntity.ok(Map.of("slot", slot, "imageUrl", imageUrl));
    }
}
