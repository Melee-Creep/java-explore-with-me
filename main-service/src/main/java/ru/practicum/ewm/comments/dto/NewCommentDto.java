package ru.practicum.ewm.comments.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * DTO for {@link ru.practicum.ewm.comments.model.Comment}
 */
public record NewCommentDto(
        @Size(message = "Коментарий должен содержать от 1 до 7000 символов", min = 1, max = 7000)
        @NotEmpty(message = "Текс коментария не должен быть пустой") String text) {
}