package ru.practicum.ewm.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.mapper.RequestMapper;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.RequestStatus;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestMapper requestMapper;

    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Нельзя подать заявку на участие в своём событие");
        }
        if (event.getState().name().equals(EventState.PENDING.name())) {
            throw new ConflictException("Данное событие не опубликовано");
        }
        if (event.getRequestModeration().equals(false) && event.getParticipantLimit() > 0
                && requestRepository.countByEventId(eventId) >= event.getParticipantLimit()) {
            throw new ConflictException("Количество участноков достигло максимума");
        }

        Request request = Request.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(user)
                .build();
        RequestStatus status;
        if (event.getParticipantLimit() == 0) {
            status = RequestStatus.CONFIRMED;
        } else {
            status = RequestStatus.PENDING;
        }
        request.setStatus(status);

        return requestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос на участие не найден"));

        if (!request.getRequester().getId().equals(userId)) {
            throw new NotFoundException("Доступ запрещён для пользователя с id=" + userId);
        }
        request.setStatus(RequestStatus.CANCELED);
        return requestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        List<Request> requests = requestRepository.findByRequesterId(userId);

        return requests.stream()
                .map(requestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }


    public List<ParticipationRequestDto> getRequestsForUserAndEvent(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Собитие не найдено"));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new BadRequestException("Только инициатор может просматривать запросы на события");
        }

        return requestRepository.findAllByEventId(eventId).stream()
                .map(requestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }
}
