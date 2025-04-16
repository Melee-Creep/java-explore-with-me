package ru.practicum.server.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;
import ru.practicum.server.model.Hit;
import ru.practicum.server.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final HitRepository hitRepository;

    @Override
    public void saveHit(EndpointHit endpointHit) {
        Hit hit = Hit.builder()
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .ip(endpointHit.getIp())
                .timestamp(endpointHit.getTimestamp())
                .build();

        hitRepository.save(hit);
    }

    @Override
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        return unique ? hitRepository.getUniqueStats(start, end, uris)
                : hitRepository.getStats(start, end, uris);
    }
}
