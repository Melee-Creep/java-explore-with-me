package ru.practicum.ewm.comments.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.practicum.ewm.comments.dto.CommentDto;
import ru.practicum.ewm.comments.dto.NewCommentDto;
import ru.practicum.ewm.comments.model.Comment;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.user.mapper.UserMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, EventMapper.class})
public interface CommentMapper {
    Comment toEntity(NewCommentDto newCommentDto);

    CommentDto toCommentDto(Comment comment);
}