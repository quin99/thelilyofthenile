package com.thelilyofthenile.backend.repository;

import com.thelilyofthenile.backend.model.Category;
import com.thelilyofthenile.backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);
}
