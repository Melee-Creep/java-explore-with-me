package ru.practicum.ewm.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserShortDto;
import ru.practicum.ewm.user.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toEntity(UserDto userDto);

    UserDto toUserDto(User user);

    User toEntity(NewUserRequest newUserRequest);

    NewUserRequest toNewUserRequest(User user);

    User toEntity(UserShortDto userShortDto);

    UserShortDto toUserShortDto(User user);
}