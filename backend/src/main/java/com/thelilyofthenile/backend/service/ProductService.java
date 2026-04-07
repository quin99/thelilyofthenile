package com.thelilyofthenile.backend.service;

import com.thelilyofthenile.backend.dto.ProductRequestDTO;
import com.thelilyofthenile.backend.dto.ProductResponseDTO;
import com.thelilyofthenile.backend.model.Category;
import com.thelilyofthenile.backend.model.Product;
import com.thelilyofthenile.backend.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository repo;
    private final String uploadDir = "uploads/"; // folder to store images

    public ProductService(ProductRepository repo) {
        this.repo = repo;
        createUploadDir();
    }

        // Overloaded create
    public ProductResponseDTO create(ProductRequestDTO dto) {
        return create(dto, null);
    }

    // Overloaded update
    public ProductResponseDTO update(Long id, ProductRequestDTO dto) {
        return update(id, dto, null);
}

    // --- PUBLIC METHODS ---

    public List<ProductResponseDTO> getAll() {
        return repo.findAll().stream().map(this::toResponseDTO).toList();
    }

    public List<ProductResponseDTO> getByCategory(String category) {
        return repo.findByCategory(Category.valueOf(category.toUpperCase()))
                .stream().map(this::toResponseDTO).toList();
    }

    public ProductResponseDTO getById(Long id) {
        return repo.findById(id).map(this::toResponseDTO).orElse(null);
    }

    public ProductResponseDTO create(ProductRequestDTO dto, MultipartFile file) {
        Product product = toEntity(dto);

        if (file != null && !file.isEmpty()) {
            String imageUrl = saveFile(file);
            product.setImageUrl(imageUrl);
        }

        return toResponseDTO(repo.save(product));
    }

    public ProductResponseDTO update(Long id, ProductRequestDTO dto, MultipartFile file) {
        Product product = repo.findById(id).orElse(null);
        if (product == null) return null;

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setCategory(dto.getCategory());
        product.setStock(dto.getStock());

        if (file != null && !file.isEmpty()) {
            String imageUrl = saveFile(file);
            product.setImageUrl(imageUrl);
        } else {
            product.setImageUrl(dto.getImageUrl());
        }

        return toResponseDTO(repo.save(product));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    // --- PRIVATE HELPERS ---

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

    private String saveFile(MultipartFile file) {
        try {
            String extension = getFileExtension(file.getOriginalFilename());
            String filename = UUID.randomUUID() + "." + extension;
            Path filepath = Paths.get(uploadDir, filename);
            Files.write(filepath, file.getBytes());
            return "/uploads/" + filename; // URL path for frontend
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf('.') + 1);
    }

    private void createUploadDir() {
        try {
            Path path = Paths.get(uploadDir);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create upload directory", e);
        }
    }
}