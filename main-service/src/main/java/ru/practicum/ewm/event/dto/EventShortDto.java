package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.time.LocalDateTime;

/**
 * DTO for {@link ru.practicum.ewm.event.model.Event}
 */
public record EventShortDto(Long id,
                            String annotation,
                            String title,
                            CategoryDto category,
                            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                            LocalDateTime eventDate,
                            UserShortDto initiator,
                            Boolean paid,
                            Integer views,
                            Long confirmedRequests) {
}