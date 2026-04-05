package com.thelilyofthenile.backend.dto;

import com.thelilyofthenile.backend.model.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProductRequestDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    @Positive
    private Double price;

    private String imageUrl;

    @NotNull
    private Category category;

    @NotNull
    @Min(0)
    private Integer stock;
}
