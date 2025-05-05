package ru.practicum.ewm.comments.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.time.LocalDateTime;

/**
 * DTO for {@link ru.practicum.ewm.comments.model.Comment}
 */
public record CommentDto(Long id,
                         String text,

                         @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                         LocalDateTime created,

                         @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                         LocalDateTime update,

                         UserShortDto user,
                         EventShortDto event) {
}