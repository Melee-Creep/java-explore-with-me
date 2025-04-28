package ru.practicum.ewm.event.model;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.event.dto.AdminSearchEventsParamsDto;
import ru.practicum.ewm.event.dto.PublicSearchEventsParamsDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Component
public class EventSpecification {

    public static Specification<Event> withAdminFilters(AdminSearchEventsParamsDto params) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            //Фильтрация по пользователям
            if (params.getUserIds() != null && !params.getUserIds().isEmpty()) {
                predicates.add(root.get("initiator").get("id").in(params.getUserIds()));
            }

            //Фильтрация по статусу события
            if (params.getStates() != null && !params.getStates().isEmpty()) {
                predicates.add(root.get("state").in(params.getStates()));
            }

            // Фильтрация по категориям
            if (params.getCategoriesIds() != null && !params.getCategoriesIds().isEmpty()) {
                predicates.add(root.get("category").get("id").in(params.getCategoriesIds()));
            }

            // Фильтрация по дате начала
            if (params.getRangeStart() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("eventDate"), params.getRangeStart()));
            }

            // Фильтрация по дате окончания
            if (params.getRangeEnd() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("eventDate"), params.getRangeEnd()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


    public static Specification<Event> withPublicFilters(PublicSearchEventsParamsDto params) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Только опубликованные события
            predicates.add(cb.equal(root.get("state"), EventState.PUBLISHED));

            // Поиск по тексту (в аннотации или описании)
            if (params.getText() != null && !params.getText().isBlank()) {
                String pattern = "%" + params.getText().toLowerCase() + "%";
                Predicate annotationPredicate = cb.like(cb.lower(root.get("annotation")), pattern);
                Predicate descriptionPredicate = cb.like(cb.lower(root.get("description")), pattern);
                predicates.add(cb.or(annotationPredicate, descriptionPredicate));
            }

            // Фильтрация по категориям
            if (params.getCategories() != null && !params.getCategories().isEmpty()) {
                predicates.add(root.get("category").get("id").in(params.getCategories()));
            }

            // Фильтрация по платности
            if (params.getPaid() != null) {
                predicates.add(cb.equal(root.get("paid"), params.getPaid()));
            }

            // Фильтрация по дате начала
            if (params.getRangeStart() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("eventDate"), params.getRangeStart()));
            } else {
                predicates.add(cb.greaterThan(root.get("eventDate"), LocalDateTime.now()));
            }

            // Фильтрация по дате окончания
            if (params.getRangeEnd() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("eventDate"), params.getRangeEnd()));
            }

            // Только доступные события
            if (Boolean.TRUE.equals(params.getOnlyAvailable())) {
                predicates.add(cb.or(
                        cb.equal(root.get("participantLimit"), 0),
                        cb.greaterThan(root.get("participantLimit"), root.get("confirmedRequests"))
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


}
