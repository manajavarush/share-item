package com.project.shareitem.controller;

import com.project.shareitem.dto.ItemCreateUpdateDto;
import com.project.shareitem.dto.ItemResponseDto;
import com.project.shareitem.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemResponseDto> createItem(@RequestBody @Valid ItemCreateUpdateDto itemDto,
                                                      @RequestHeader(value = "X-Sharer-User-Id") Long ownerId) {
        var createdItem = itemService.createItem(itemDto, ownerId);

        return ResponseEntity.ok(createdItem);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemResponseDto> updateItem(@PathVariable Long itemId,
                                                      @RequestBody ItemCreateUpdateDto itemDto,
                                                      @RequestHeader(value = "X-Sharer-User-Id") Long ownerId) {
        var updatedItem = itemService.updateItem(itemId, itemDto, ownerId);

        return ResponseEntity.ok(updatedItem);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponseDto> getItemById(@PathVariable Long itemId) {
        var itemResponseDto = itemService.getItemById(itemId);

        return ResponseEntity.ok(itemResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<ItemResponseDto>> getAllItemsByUser(
            @RequestHeader(value = "X-Sharer-User-Id") Long ownerId) {
        var itemResponseDtos = itemService.getAllItemsByUserId(ownerId);

        return ResponseEntity.ok(itemResponseDtos);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemResponseDto>> searchItems(@RequestParam String text) {
        var itemResponseDtos = itemService.searchItems(text);

        return ResponseEntity.ok(itemResponseDtos);
    }
}
