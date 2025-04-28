package ru.practicum.ewm.request.mapper;

import org.mapstruct.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.model.Request;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RequestMapper {
    @Mapping(source = "requester", target = "requester.id")
    @Mapping(source = "event", target = "event.id")
    Request toEntity(ParticipationRequestDto participationRequestDto);

    @InheritInverseConfiguration(name = "toEntity")
    ParticipationRequestDto toParticipationRequestDto(Request request);
}