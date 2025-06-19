package com.project.shareitem.mapper;

import com.project.shareitem.dto.UserDto;
import com.project.shareitem.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") // добавляем UserMapper в контекст для дальнейшего @Autowired
public interface UserMapper {
    // Преобразует email к lowercase и обрезает пробелы
    // @Mapping(target = "email", expression = "java(userDTO.getEmail().toLowerCase().trim())")
    User userDtoToUser(UserDto userDTO);

    UserDto userToUserDto(User user);
}
