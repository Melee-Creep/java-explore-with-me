package ru.practicum.ewm.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * DTO for {@link ru.practicum.ewm.compilation.model.Compilation}
 */
public record NewCompilationDto(@Size(min = 1, max = 50)
                                @NotBlank String title,
                                boolean pinned,
                                List<Long> events) {
}