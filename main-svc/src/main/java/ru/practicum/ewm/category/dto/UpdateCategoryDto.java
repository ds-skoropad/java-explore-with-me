package ru.practicum.ewm.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

public record UpdateCategoryDto(
        @Null
        Long id,
        @NotBlank @Size(min = 1, max = 50)
        String name
) {
}
