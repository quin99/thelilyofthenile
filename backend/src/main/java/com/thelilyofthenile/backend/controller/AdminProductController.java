package com.thelilyofthenile.backend.controller;

import com.thelilyofthenile.backend.dto.ProductRequestDTO;
import com.thelilyofthenile.backend.dto.ProductResponseDTO;
import com.thelilyofthenile.backend.model.Category;
import com.thelilyofthenile.backend.service.ImageStorageService;
import com.thelilyofthenile.backend.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/products")
public class AdminProductController {

    private final ProductService productService;
    private final ImageStorageService imageStorageService;

    public AdminProductController(ProductService productService,
                                   ImageStorageService imageStorageService) {
        this.productService = productService;
        this.imageStorageService = imageStorageService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAll() {
        return ResponseEntity.ok(productService.getAll());
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ProductResponseDTO> create(
            @RequestPart("product") Map<String, Object> fields,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        ProductRequestDTO dto = buildDto(fields, image, null);
        return ResponseEntity.ok(productService.create(dto));
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ProductResponseDTO> update(
            @PathVariable Long id,
            @RequestPart("product") Map<String, Object> fields,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        String existingImageUrl = (String) fields.get("existingImageUrl");
        ProductRequestDTO dto = buildDto(fields, image, existingImageUrl);
        ProductResponseDTO updated = productService.update(id, dto);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private ProductRequestDTO buildDto(Map<String, Object> fields, MultipartFile image, String existingImageUrl) throws IOException {
        String imageUrl = existingImageUrl;
        if (image != null && !image.isEmpty()) {
            imageUrl = imageStorageService.store(image);
        }

        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setName((String) fields.get("name"));
        dto.setDescription((String) fields.get("description"));
        dto.setPrice(((Number) fields.get("price")).doubleValue());
        dto.setCategory(Category.valueOf(((String) fields.get("category")).toUpperCase()));
        dto.setStock(((Number) fields.get("stock")).intValue());
        dto.setImageUrl(imageUrl);
        return dto;
    }
}
