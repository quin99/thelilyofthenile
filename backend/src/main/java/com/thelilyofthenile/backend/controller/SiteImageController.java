package com.thelilyofthenile.backend.controller;

import com.thelilyofthenile.backend.model.SiteImage;
import com.thelilyofthenile.backend.repository.SiteImageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/site-images")
public class SiteImageController {

    private final SiteImageRepository repo;

    public SiteImageController(SiteImageRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> getAll() {
        Map<String, String> result = new HashMap<>();
        for (SiteImage img : repo.findAll()) {
            result.put(img.getSlot(), img.getImageUrl());
        }
        return ResponseEntity.ok(result);
    }
}
