package ru.practicum.ewm.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDto(
        Long id,

        @NotBlank(message = "Имя должно быть указанно")
        @Size(min = 2, max = 250, message = "Введите корректное имя") String name,

        @NotBlank(message = "Email обязателен")
        @Email(message = "Введите корректный email")
        @Size(min = 6, max = 254, message = "Введите корректный email") String email) {
}