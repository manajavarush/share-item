package com.project.shareitem.service;

import com.project.shareitem.dto.ItemCreateUpdateDto;
import com.project.shareitem.dto.ItemResponseDto;
import com.project.shareitem.mapper.ItemMapper;
import com.project.shareitem.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final ValidationService validationService;


    public ItemResponseDto createItem(ItemCreateUpdateDto createDto, Long userId) {
        var user = validationService.validateUserExists(userId);

        var item = itemMapper.toEntity(createDto);

        item.setOwner(user);

        var savedItem = itemRepository.save(item);

        log.info("Пользователь с id: {} добавил новую вещь с id: {}", userId, savedItem.getId());
        return itemMapper.toResponseDto(savedItem);
    }

    public ItemResponseDto updateItem(Long itemId, ItemCreateUpdateDto updateDto, Long userId) {
        var item = validationService.validateItemExists(itemId);

        validationService.validateUserExists(userId);

        validationService.validateItemOwner(itemId, userId);

        if (updateDto.getName() != null) {
            item.setName(updateDto.getName());
        }

        if (updateDto.getDescription() != null) {
            item.setDescription(updateDto.getDescription());
        }

        if (updateDto.getAvailable() != null) {
            item.setAvailable(updateDto.getAvailable());
        }

        var savedItem = itemRepository.save(item);
        log.info("Пользователь с id: {} обновил данные вещи с id: {}", userId, itemId);

        return itemMapper.toResponseDto(savedItem);
    }


    public ItemResponseDto getItemById(Long itemId) {
        var item = validationService.validateItemExists(itemId);
        log.info("Получена вещь с itemId: {}", itemId);

        return itemMapper.toResponseDto(item);
    }

    public List<ItemResponseDto> getAllItemsByUserId(Long ownerId) {
        validationService.validateUserExists(ownerId);

        var allItemsByUserId = itemRepository.findAllByOwnerId(ownerId);
        log.info("Получен список всех вещей пользователя с id: {}", ownerId);

        return allItemsByUserId.stream()
                .map(itemMapper::toResponseDto)
                .toList();
    }

    public List<ItemResponseDto> searchItems(String text) {
        if (text.trim().isBlank()) {
            log.info("По данному запросу: {} нет доступных вещей", text);
            return Collections.emptyList();
        }

        var availableItems = itemRepository.searchAvailableItems(text);
        log.info("Получен список всех доступных вещей по запросу: {}", text);

        return availableItems.stream()
                .map(itemMapper::toResponseDto)
                .toList();
    }
}
