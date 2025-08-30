package com.project.shareitem.controller;

import com.project.shareitem.dto.CommentDto;
import com.project.shareitem.dto.ItemBookingStatusCommentDto;
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
    public ResponseEntity<ItemBookingStatusCommentDto> getItemById(@PathVariable Long itemId,
                                                                   @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        var itemResponseDto = itemService.getItemById(itemId, userId);

        return ResponseEntity.ok(itemResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<ItemBookingStatusCommentDto>> getAllItemsByUser(
            @RequestHeader(value = "X-Sharer-User-Id") Long ownerId) {
        var itemBookingStatusDtos = itemService.getAllItemsByUserId(ownerId);

        return ResponseEntity.ok(itemBookingStatusDtos);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemResponseDto>> searchItems(@RequestParam String text) {
        var itemResponseDtos = itemService.searchItems(text);

        return ResponseEntity.ok(itemResponseDtos);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> createComment(@PathVariable Long itemId,
                                                    @RequestBody CommentDto commentDto,
                                                    @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        var comment = itemService.createComment(itemId, commentDto, userId);

        return ResponseEntity.ok(comment);
    }
}
