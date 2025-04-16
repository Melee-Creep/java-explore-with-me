package ru.practicum.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ViewStats {

    private String app;
    private String uri;
    private Long hits;
}
