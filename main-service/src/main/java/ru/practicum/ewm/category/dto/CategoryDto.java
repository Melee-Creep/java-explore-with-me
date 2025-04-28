package ru.practicum.ewm.category.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryDto(
        Long id,

        @NotBlank(message = "Название котегории должно быть указанно")
        @Size(max = 50, message = "Название категории не должно превышать 50 символов") String name) {
}