package ru.practicum.ewm.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class RestErrorHandler  {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException e) {

        ApiError apiError = ApiError.builder()
                .errors(List.of(e.getMessage()))
                .message("Ошибка валидации полей")
                .reason("Неверно заполнены данные запроса")
                .status(HttpStatus.BAD_REQUEST.name())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(apiError);
    }


    @ExceptionHandler
    public ResponseEntity<ApiError> notFoundException(final NotFoundException e) {
        ApiError error = ApiError.builder()
                .errors(List.of(e.getMessage()))
                .message("Запрашиваемый объект не найден")
                .reason("Объект с указанным идентификатором не существует")
                .status(HttpStatus.NOT_FOUND.name())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> conflictException(final ConflictException e) {
        ApiError error = ApiError.builder()
                .errors(List.of(e.getMessage()))
                .message("Создаваемый объект не удовлетворяет условиям")
                .reason("Объект с указанным идентификатором не подходит")
                .status(HttpStatus.NOT_FOUND.name())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleUniqueConstraintViolation(DataIntegrityViolationException e) {

        ApiError error = ApiError.builder()
                .errors(List.of(e.getMessage()))
                .message("Категория с таким именем уже существует")
                .reason("Нарушено уникальное ограничениет")
                .status(HttpStatus.CONFLICT.name())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> timeException(final BadRequestException e) {
        ApiError error = ApiError.builder()
                .errors(List.of(e.getMessage()))
                .message("Создаваемый объект не удовлетворяет условиям")
                .reason("Объект с указанным идентификатором не подходит")
                .status(HttpStatus.NOT_FOUND.name())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

}