package com.thelilyofthenile.backend.service;

import com.thelilyofthenile.backend.dto.ProductRequestDTO;
import com.thelilyofthenile.backend.dto.ProductResponseDTO;
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

    public List<ProductResponseDTO> getAll() {
        return repo.findAll().stream().map(this::toResponseDTO).toList();
    }

    public List<ProductResponseDTO> getByCategory(String category) {
        return repo.findAll().stream()
                .filter(p -> p.getCategory() != null &&
                             p.getCategory().name().equalsIgnoreCase(category))
                .map(this::toResponseDTO)
                .toList();
    }

    public ProductResponseDTO getById(Long id) {
        return repo.findById(id).map(this::toResponseDTO).orElse(null);
    }

    public ProductResponseDTO create(ProductRequestDTO dto) {
        Product product = toEntity(dto);
        return toResponseDTO(repo.save(product));
    }

    public ProductResponseDTO update(Long id, ProductRequestDTO dto) {
        Product product = repo.findById(id).orElse(null);
        if (product == null) return null;

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setImageUrl(dto.getImageUrl());
        product.setCategory(dto.getCategory());
        product.setStock(dto.getStock());

        return toResponseDTO(repo.save(product));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    private Product toEntity(ProductRequestDTO dto) {
        return Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .imageUrl(dto.getImageUrl())
                .category(dto.getCategory())
                .stock(dto.getStock())
                .build();
    }

    private ProductResponseDTO toResponseDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setImageUrl(product.getImageUrl());
        dto.setCategory(product.getCategory());
        dto.setStock(product.getStock());
        return dto;
    }
}
