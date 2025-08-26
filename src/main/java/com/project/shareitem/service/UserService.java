package com.project.shareitem.service;

import com.project.shareitem.dto.UserDto;
import com.project.shareitem.entity.User;
import com.project.shareitem.exception.UserNotFoundException;
import com.project.shareitem.mapper.UserMapper;
import com.project.shareitem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final ValidationService validationService;

    public User createUser(UserDto userDto) {
        validationService.validateEmailNotExists(userDto.email());

        User user = userMapper.userDtoToUser(userDto);
        log.info("Создан пользователь с email: {}", userDto.email());

        return userRepository.save(user);
    }

    public User getUserById(Long userId) {
        var user = validationService.validateUserExists(userId);
        log.info("Получен пользователь с id: {}", userId);

        return user;
    }

    public User updateUser(Long userId, UserDto userDto) {
        User existingUser = getUserById(userId);

        if (userDto.name() != null) {
            existingUser.setName(userDto.name());
        }

        if (userDto.email() != null && !existingUser.getEmail().equals(userDto.email())) {
            validationService.validateEmailNotExists(userDto.email());
            existingUser.setEmail(userDto.email());
        }

        log.info("Данные пользователя с id: {} обновлены", userId);

        return existingUser;
    }

    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            log.warn("Пользователь с id: {} не найден", userId);
            throw new UserNotFoundException(userId);
        }

        userRepository.deleteById(userId);
        log.info("Пользователь с id: {} удален", userId);
    }
}
