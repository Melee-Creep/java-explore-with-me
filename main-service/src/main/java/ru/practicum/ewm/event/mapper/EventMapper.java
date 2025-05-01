package ru.practicum.ewm.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.mapper.UserMapper;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {CategoryMapper.class, UserMapper.class})
public interface EventMapper {
    Event toEntity(EventFullDto eventFullDto);

    EventFullDto toEventFullDto(Event event);

    Event toEntity(EventShortDto eventShortDto);

    EventShortDto toEventShortDto(Event event);

    @Mapping(source = "category", target = "category.id")
    Event toEntity(NewEventDto newEventDto);

    @Mapping(source = "category.id", target = "category")
    NewEventDto toNewEventDto(Event event);

    @Mapping(source = "categoryId", target = "category.id")
    Event toEntity(UpdateEventUserRequest updateEventUserRequest);

    @Mapping(source = "category.id", target = "categoryId")
    UpdateEventUserRequest toUpdateEventUserRequest(Event event);

    @Mapping(source = "categoryId", target = "category.id")
    Event toEntity(UpdateEventAdminRequest updateEventAdminRequest);

    @Mapping(source = "category.id", target = "categoryId")
    UpdateEventAdminRequest toUpdateEventAdminRequest(Event event);
}