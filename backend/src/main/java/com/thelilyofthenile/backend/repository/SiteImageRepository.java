package com.thelilyofthenile.backend.repository;

import com.thelilyofthenile.backend.model.SiteImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SiteImageRepository extends JpaRepository<SiteImage, Long> {
    Optional<SiteImage> findBySlot(String slot);
}
