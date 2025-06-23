package com.project.shareitem.service;

import com.project.shareitem.dto.UserDto;
import com.project.shareitem.entity.User;

public interface UserService {

    User createUser(UserDto userDTO);

    User getUserById(Long userId);

    void deleteUser(Long userId);

    User updateUser(Long userId, UserDto userDto);
}
