package com.project.shareitem.service;

import com.project.shareitem.dto.ItemCreateUpdateDto;
import com.project.shareitem.dto.ItemResponseDto;

import java.util.List;

public interface ItemService {
    ItemResponseDto createItem(ItemCreateUpdateDto createDto, Long userId);

    ItemResponseDto updateItem(Long itemId, ItemCreateUpdateDto updateDto, Long userId);

    ItemResponseDto getItemById(Long id);

    List<ItemResponseDto> getAllItemsByUserId(Long userId);

    List<ItemResponseDto> searchItems(String text);
}
