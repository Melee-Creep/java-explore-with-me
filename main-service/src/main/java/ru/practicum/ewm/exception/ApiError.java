package ru.practicum.ewm.exception;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ApiError {

    private List<String> errors;
    private String message;
    private String reason;
    private String status;
    private LocalDateTime timestamp;
}
