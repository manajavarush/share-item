package com.project.shareitem.service;

import com.project.shareitem.dto.UserDto;
import com.project.shareitem.exception.EmailAlreadyExistsException;
import com.project.shareitem.exception.UserNotFoundException;
import com.project.shareitem.mapper.UserMapper;
import com.project.shareitem.model.User;
import com.project.shareitem.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public User createUser(UserDto userDto) {

        if (userRepository.existsByEmail(userDto.getEmail())) {
            log.warn("Email {} уже используется", userDto.getEmail());
            throw new EmailAlreadyExistsException(userDto.getEmail());
        }

        User user = userMapper.userDtoToUser(userDto);
        log.info("Создан пользователь с email: {}", userDto.getEmail());

        return userRepository.save(user);
    }

    public User getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Пользователь с id: {} не найден", userId);
                    return new UserNotFoundException(userId);
                });

        log.info("Получен пользователь с id: {}", userId);
        return user;
    }

    public User updateUser(Long userId, UserDto userDto) {
        User existingUser = getUserById(userId);

        if(userDto.getName() != null) {
            existingUser.setName(userDto.getName());
        }

        if (userDto.getEmail() != null && !existingUser.getEmail().equals(userDto.getEmail())) {

            if (userRepository.existsByEmail(userDto.getEmail())) {
                log.warn("Email {} уже используется", userDto.getEmail());
                throw new EmailAlreadyExistsException(userDto.getEmail());
            }

            existingUser.setEmail(userDto.getEmail());
        }

        userRepository.update(existingUser);
        log.info("Данные пользователя с id: {} обновлены", userId);

        return existingUser;
    }

    public void deleteUser(Long userId) {
        if (!userRepository.delete(userId)) {
            log.warn("Пользователь с id: {} не найден", userId);
            throw new UserNotFoundException(userId);
        }

        log.info("Пользователь с id: {} удален", userId);
    }
}
