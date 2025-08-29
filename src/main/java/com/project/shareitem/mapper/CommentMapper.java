package com.project.shareitem.mapper;

import com.project.shareitem.dto.CommentDto;
import com.project.shareitem.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "authorName", source = "author.name")
    CommentDto toCommentDto(Comment comment);
}
