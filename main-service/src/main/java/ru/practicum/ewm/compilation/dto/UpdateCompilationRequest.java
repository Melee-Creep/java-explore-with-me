package ru.practicum.ewm.compilation.dto;

import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * DTO for {@link ru.practicum.ewm.compilation.model.Compilation}
 */
public record UpdateCompilationRequest(@Size(min = 1, max = 50) String title,
                                       boolean pinned,
                                       List<Long> events) {
}