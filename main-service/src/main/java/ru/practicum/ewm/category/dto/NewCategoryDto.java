package ru.practicum.ewm.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for {@link ru.practicum.ewm.category.model.Category}
 */
public record NewCategoryDto(
        @Size (max = 50, message = "Название категории не должно превышать 50 символов")
        @NotBlank(message = "Название категории должно быть указанно") String name) {
}