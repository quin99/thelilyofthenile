package com.thelilyofthenile.backend.service;

import com.thelilyofthenile.backend.model.Product;
import com.thelilyofthenile.backend.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public List<Product> getAll() {
        return repo.findAll();
    }

    public Product getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Product save(Product product) {
        return repo.save(product);
    }

    public Product update(Long id, Product updated) {
        Product p = getById(id);
        if (p == null) return null;

        p.setName(updated.getName());
        p.setDescription(updated.getDescription());
        p.setPrice(updated.getPrice());
        p.setImageUrl(updated.getImageUrl());

        return repo.save(p);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
