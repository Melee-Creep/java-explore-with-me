package ru.practicum.ewm.compilation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.model.Event;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CompilationMapper {
    Compilation toEntity(CompilationDto compilationDto);

    CompilationDto toCompilationDto(Compilation compilation);

    @Mapping(target = "events", ignore = true)
    Compilation toEntity(UpdateCompilationRequest updateCompilationRequest);

    @Mapping(target = "events", expression = "java(eventsToEventIds(compilation.getEvents()))")
    UpdateCompilationRequest toUpdateCompilationRequest(Compilation compilation);

    default List<Long> eventsToEventIds(List<Event> events) {
        return events.stream().map(Event::getId).toList();
    }
}