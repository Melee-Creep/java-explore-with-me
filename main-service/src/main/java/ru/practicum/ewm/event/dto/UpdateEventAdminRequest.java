package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.event.model.StateAction;

import java.time.LocalDateTime;

/**
 * DTO for {@link ru.practicum.ewm.event.model.Event}
 */
public record UpdateEventAdminRequest(
        @Size(message = "Описание должно содержать от 20 до 2000 символов", min = 20, max = 2000)
        String annotation,

        @Size(message = "Заголовок должен содержать от 3 до 120 символов", min = 3, max = 120)
        String title,

        @Size(min = 20, max = 7000, message = "Описание должно содержать от 20 до 7000 символов")
        String description,
        Long categoryId,
        @Future(message = "Дата события указана неверно")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime eventDate,
        Location location,
        Boolean paid,
        Integer participantLimit,
        Boolean requestModeration,
        StateAction stateAction) {
}