package ru.practicum.ewm.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for {@link ru.practicum.ewm.user.model.User}
 */
public record NewUserRequest(
        @Size(message = "Введите корректное имя", min = 2, max = 250)
        @NotBlank(message = "Имя должно быть указано") String name,

        @Size(message = "Введите корректный email", min = 6, max = 254)
        @Email(message = "Введите корректный email")
        @NotBlank(message = "Введите корректный email") String email) {
}