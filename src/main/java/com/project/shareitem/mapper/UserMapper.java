package com.project.shareitem.mapper;

import com.project.shareitem.dto.UserDto;
import com.project.shareitem.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserDto userDTO);
}
