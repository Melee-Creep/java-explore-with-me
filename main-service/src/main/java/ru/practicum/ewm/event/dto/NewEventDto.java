package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import ru.practicum.ewm.event.model.Location;

import java.time.LocalDateTime;

public record NewEventDto(
                          @NotBlank(message = "Описание должно быть указано")
                          @Size(min = 20, max = 2000, message = "Описание должно быть от 20 до 2000 символов")
                          String annotation,

                          @Size(min = 3, max = 120, message = "Заголовок должен быть от 7 до 120 символов")
                          String title,

                          @NotBlank(message = "Описание должно быть указано")
                          @Size(min = 20, max = 7000, message = "Описание должно быть от 20 до 7000 символов")
                          String description,

                          Long category,

                          @Future()
                          @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                          LocalDateTime eventDate,

                          Location location,
                          Boolean paid,

                          @PositiveOrZero(message = "Количество участников должно быть положительным")
                          Integer participantLimit,

                          Boolean requestModeration) {
}