package com.project.shareitem.mapper;

import com.project.shareitem.dto.UserDTO;
import com.project.shareitem.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") // добавляем UserMapper в контекст для дальнейшего @Autowired
public interface UserMapper {
    User userDtoToUser(UserDTO userDTO);

    UserDTO userToUserDto(User user);
}
