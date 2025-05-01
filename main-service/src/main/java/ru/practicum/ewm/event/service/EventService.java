package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.*;

import java.util.List;

public interface EventService {

    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    List<EventShortDto> getUserEvents(Long userId, int from, int size);

    EventFullDto getUserEvent(Long userId, Long eventId);

    EventFullDto updateUserEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<EventFullDto> getEventsByAdmin(AdminSearchEventsParamsDto adminSearchEventsParamsDto);

    EventFullDto updateAdminEvent(Long eventId, UpdateEventAdminRequest adminRequest);

    List<EventShortDto> getPublicEvents(PublicSearchEventsParamsDto publicSearchEventsParamsDto);

    EventFullDto getEventById(Long eventId, String ipAddress);

    EventRequestStatusUpdateResult changeStatusEvent(Long userId, Long eventId, EventRequestStatusUpdateRequest request);

}
