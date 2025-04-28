package ru.practicum.server.exception;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ApiErrorStats {

    private String error;
    private String description;
}
