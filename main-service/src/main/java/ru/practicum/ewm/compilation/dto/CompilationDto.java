package ru.practicum.ewm.compilation.dto;

import ru.practicum.ewm.event.dto.EventShortDto;

import java.util.List;

/**
 * DTO for {@link ru.practicum.ewm.compilation.model.Compilation}
 */
public record CompilationDto(Long id,
                             String title,
                             boolean pinned,
                             List<EventShortDto> events) {
}