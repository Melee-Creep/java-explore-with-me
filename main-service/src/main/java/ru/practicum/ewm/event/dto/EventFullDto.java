package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.time.LocalDateTime;

public record EventFullDto(Long id,
                           String annotation,
                           String title,
                           String description,
                           LocalDateTime createdOn,
                           CategoryDto category,

                           @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                           LocalDateTime eventDate,
                           @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                           LocalDateTime publishedOn,

                           UserShortDto initiator,
                           Location location,
                           Boolean paid,
                           Integer participantLimit,
                           Boolean requestModeration,
                           EventState state,
                           Integer views,
                           Long confirmedRequests) {
}