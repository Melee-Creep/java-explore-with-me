package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.service.CategoryService;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventSpecification;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.EventView;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.repository.EventViewRepository;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.mapper.RequestMapper;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.RequestStatus;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final RequestRepository requestRepository;
    private final EventViewRepository eventViewRepository;

    private final EventMapper eventMapper;
    private final CategoryMapper categoryMapper;
    private final RequestMapper requestMapper;


    @Override
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        User user = userService.getUserById(userId);

        CategoryDto categoryDto = categoryService.findById(newEventDto.category());

        Event event = eventMapper.toEntity(newEventDto);
        event.setInitiator(user);
        event.setCategory(categoryMapper.toEntity(categoryDto));
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        event.setViews(0);
        event.setConfirmedRequests(0L);

        if (newEventDto.paid() == null) {
            event.setPaid(false);
        }
        if (newEventDto.requestModeration() == null) {
            event.setRequestModeration(true);
        }
        if (newEventDto.participantLimit() == null) {
            event.setParticipantLimit(0);
        }

        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventShortDto> getUserEvents(Long userId, int from, int size) {
        userService.getUserById(userId);

        Pageable pageable = PageRequest.of(from / size, size);
        List<Event> event = eventRepository.findByinitiator_id(userId, pageable);
        return event.stream()
                .map(eventMapper::toEventShortDto)
                .toList();
    }

    @Override
    public EventFullDto getUserEvent(Long userId, Long eventId) {
        User user = userService.getUserById(userId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));
        if (!user.getId().equals(event.getInitiator().getId())) {
            throw new NotFoundException("Просмотр события не доступен");
        }
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto updateUserEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        EventFullDto eventFullDto = getUserEvent(userId, eventId);
        Event event = eventMapper.toEntity(eventFullDto);
        if (updateEventUserRequest.eventDate() != null) {
            event.setEventDate(updateEventUserRequest.eventDate());
        }
        if (updateEventUserRequest.stateAction() != null) {
            if (updateEventUserRequest.stateAction().name().equals("CANCEL_REVIEW")) {
                event.setState(EventState.CANCELED);
            } else if (updateEventUserRequest.stateAction().name().equals("SEND_TO_REVIEW")) {
                event.setState(EventState.PENDING);
            }
        }
        if (event.getState().name().equals(EventState.PUBLISHED.name())) {
            throw new ConflictException("Опубликованое событие менять нельзя");
        }
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventFullDto> getEventsByAdmin(AdminSearchEventsParamsDto dto) {
        Pageable pageable = PageRequest.of(dto.getFrom() / dto.getSize(), dto.getSize());
        Specification<Event> spec = EventSpecification.withAdminFilters(dto);
        List<Event> events = eventRepository.findAll(spec, pageable).getContent();
        return events.stream()
                .map(eventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }


    @Override
    public EventFullDto updateAdminEvent(Long eventId, UpdateEventAdminRequest adminRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Собитые не найдено"));
        if (adminRequest.eventDate() != null) {
            event.setEventDate(adminRequest.eventDate());
        }
        if (adminRequest.annotation() != null && !event.getAnnotation().equals(adminRequest.annotation())) {
            event.setAnnotation(adminRequest.annotation());
        }
        if (adminRequest.paid() != null && !event.getPaid().equals(adminRequest.paid())) {
            event.setPaid(adminRequest.paid());
        }
        if (adminRequest.description() != null && !event.getDescription().equals(adminRequest.description())) {
            event.setDescription(adminRequest.description());
        }
        if (adminRequest.title() != null && !event.getTitle().equals(adminRequest.title())) {
            event.setTitle(adminRequest.title());
        }
        if (adminRequest.participantLimit() != null && !event.getParticipantLimit().equals(adminRequest.participantLimit())) {
            event.setParticipantLimit(adminRequest.participantLimit());
        }
        if (adminRequest.stateAction() != null && event.getState().name().equals(EventState.PUBLISHED.name())) {
            throw new ConflictException("Событие уже опубликовано");
        }
        if (adminRequest.stateAction() != null && event.getState().name().equals(EventState.CANCELED.name())) {
            throw new ConflictException("Отмёненое событие публиковать нельзя");
        }
        if (adminRequest.stateAction() != null && adminRequest.stateAction().name().equals("PUBLISH_EVENT")) {
            event.setState(EventState.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());
        }
        if (adminRequest.stateAction() != null && adminRequest.stateAction().name().equals("REJECT_EVENT")) {
            event.setState(EventState.CANCELED);
        }

        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventShortDto> getPublicEvents(PublicSearchEventsParamsDto params) {
        Sort sort = Sort.unsorted();
        if ("EVENT_DATE".equalsIgnoreCase(params.getSort())) {
            sort = Sort.by("eventDate").ascending();
        } else if ("VIEWS".equalsIgnoreCase(params.getSort())) {
            sort = Sort.by("views").descending();
        }

        if (params.getRangeStart() != null && params.getRangeEnd() != null &&
                params.getRangeStart().isAfter(params.getRangeEnd())) {
            throw new BadRequestException("rangeStart must be before rangeEnd");
        }

        Pageable pageable = PageRequest.of(params.getFrom() / params.getSize(), params.getSize(), sort);

        Specification<Event> spec = EventSpecification.withPublicFilters(params);
        List<Event> events = eventRepository.findAll(spec, pageable).getContent();


        return events.stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }



    @Override
    public EventFullDto getEventById(Long eventId, String ipAddress) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));

        if (event.getState() != EventState.PUBLISHED) {
            throw new NotFoundException("Событие не найдено");
        }

        if (!eventViewRepository.existsByEventIdAndIp(eventId, ipAddress)) {
            eventViewRepository.save(new EventView(null, eventId, ipAddress));
            event.setViews(event.getViews() + 1);
            eventRepository.save(event);
        }

        return eventMapper.toEventFullDto(event);
    }


    @Override
    public EventRequestStatusUpdateResult changeStatusEvent(Long userId, Long eventId, EventRequestStatusUpdateRequest request) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Только инициатор может изменять статусы запросов");
        }

        List<Request> requests = requestRepository.findAllById(request.requestIds());

        List<ParticipationRequestDto> confirmed = new ArrayList<>();
        List<ParticipationRequestDto> rejected = new ArrayList<>();

        long confirmedCount = requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);

        for (Request r : requests) {

            if (!r.getEvent().getId().equals(eventId)) {
                throw new ConflictException("Запрос не относится к этому событию");
            }

            if (!r.getStatus().equals(RequestStatus.PENDING)) {
                throw new ConflictException("Могут быть обновлены только отложенные запросы");
            }

            if (request.status().equals("CONFIRMED")) {
                if (confirmedCount < event.getParticipantLimit()) {
                    r.setStatus(RequestStatus.CONFIRMED);
                    confirmed.add(requestMapper.toParticipationRequestDto(r));
                    confirmedCount++;
                } else {
                    r.setStatus(RequestStatus.REJECTED);
                    rejected.add(requestMapper.toParticipationRequestDto(r));
                    throw new ConflictException("Достигнут лимит участников");
                }
            } else if (request.status().equals("REJECTED")) {
                r.setStatus(RequestStatus.REJECTED);
                rejected.add(requestMapper.toParticipationRequestDto(r));
            } else {
                throw new BadRequestException("Недопустимый статус: " + request.status());
            }
        }

        requestRepository.saveAll(requests);

        event.setConfirmedRequests(confirmedCount);
        event.setPublishedOn(LocalDateTime.now());
        eventRepository.save(event);

        return new EventRequestStatusUpdateResult(confirmed, rejected);
    }


}
