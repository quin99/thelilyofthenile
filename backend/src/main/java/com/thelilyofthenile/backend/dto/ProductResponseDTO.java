package com.thelilyofthenile.backend.dto;

import com.thelilyofthenile.backend.model.Category;
import lombok.Data;

@Data
public class ProductResponseDTO {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private Category category;
    private Integer stock;
}
