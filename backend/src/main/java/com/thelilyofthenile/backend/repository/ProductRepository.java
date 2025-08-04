package com.thelilyofthenile.backend.repository;

import com.thelilyofthenile.backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
