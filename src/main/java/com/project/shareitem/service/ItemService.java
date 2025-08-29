package com.project.shareitem.service;

import com.project.shareitem.dto.CommentDto;
import com.project.shareitem.dto.ItemBookingStatusCommentDto;
import com.project.shareitem.dto.ItemCreateUpdateDto;
import com.project.shareitem.dto.ItemResponseDto;
import com.project.shareitem.entity.Comment;
import com.project.shareitem.entity.Item;
import com.project.shareitem.mapper.CommentMapper;
import com.project.shareitem.mapper.ItemMapper;
import com.project.shareitem.repository.CommentRepository;
import com.project.shareitem.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    private final ValidationService validationService;
    private final BookingUtils bookingUtils;

    private final CommentRepository commentRepository;


    @Transactional
    public ItemResponseDto createItem(ItemCreateUpdateDto createDto, Long userId) {
        var user = validationService.validateUserExists(userId);

        var item = itemMapper.toEntity(createDto);

        item.setOwner(user);

        var savedItem = itemRepository.save(item);

        log.info("Пользователь с id:{} добавил новую вещь с id:{}", userId, savedItem.getId());

        return itemMapper.toResponseDto(savedItem);
    }

    @Transactional
    public ItemResponseDto updateItem(Long itemId, ItemCreateUpdateDto updateDto, Long userId) {
        var item = validationService.validateItemExists(itemId);
        validationService.validateUserExists(userId);
        validationService.validateItemOwner(itemId, userId);

        applyUpdateData(updateDto, item);

        var savedItem = itemRepository.save(item);

        log.info("Пользователь с id:{} обновил данные вещи с id:{}", userId, itemId);

        return itemMapper.toResponseDto(savedItem);
    }

    private void applyUpdateData(ItemCreateUpdateDto updateDto, Item item) {
        if (updateDto.name() != null) {
            item.setName(updateDto.name());
        }

        if (updateDto.description() != null) {
            item.setDescription(updateDto.description());
        }

        if (updateDto.available() != null) {
            item.setAvailable(updateDto.available());
        }
    }


    @Transactional(readOnly = true)
    public ItemBookingStatusCommentDto getItemById(Long itemId, Long userId) {
        var item = validationService.validateItemExists(itemId);

        log.info("Получена вещь с itemId:{}", itemId);

        var includeBookings = Objects.equals(userId, item.getOwner().getId());

        return toBookingStatusCommentDto(item, includeBookings);
    }

    @Transactional(readOnly = true)
    public List<ItemBookingStatusCommentDto> getAllItemsByUserId(Long ownerId) {
        validationService.validateUserExists(ownerId);
        var allItemsByUserId = itemRepository.findAllByOwnerId(ownerId);

        log.info("Получен список всех вещей пользователя с id:{}", ownerId);

        return toItemBookingStatusCommentDtos(allItemsByUserId);
    }

    private List<ItemBookingStatusCommentDto> toItemBookingStatusCommentDtos(List<Item> items) {
        return items.stream()
                .map(item -> toBookingStatusCommentDto(item, true))
                .toList();
    }

    @Transactional
    public CommentDto createComment(Long itemId, CommentDto commentDto, Long userId) {
        var user = validationService.validateUserExists(userId);
        var item = validationService.validateItemExists(itemId);
        validationService.validateUserCanComment(itemId, userId);

        var comment = new Comment();

        comment.setText(commentDto.text());
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());

        var savedComment = commentRepository.save(comment);
        log.info("Пользователь с id:{} написал комментарий для вещи с id:{}", userId, itemId);

        // Проблемное место, может падать из-за LAZY инициализации поля
        // Периодически падают то Comment past booking, то get item with comments -> comments field length == 0
        // Явная инициализация LAZY-поля
        // Hibernate.initialize(savedComment.getAuthor());
        return commentMapper.toCommentDto(savedComment);

        // return toDto(savedComment);
    }


//    private CommentDto toDto(Comment comment) {
//        return new CommentDto(
//                comment.getId(),
//                comment.getText(),
//                comment.getAuthor().getName(),
//                comment.getCreated()
//        );
//    }


    private ItemBookingStatusCommentDto toBookingStatusCommentDto(Item item, boolean includeBookings) {
        var now = LocalDateTime.now();
        var itemId = item.getId();
        var commentDtos = getCommentDtos(itemId);


        return new ItemBookingStatusCommentDto(
                itemId,
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                includeBookings ? bookingUtils.findLastBookingDate(itemId, now) : null,
                includeBookings ? bookingUtils.findNextBookingDate(itemId, now) : null,
                commentDtos
        );
    }

    private List<CommentDto> getCommentDtos(Long itemId) {
        log.info("Получен список всех комментариев для вещи с id:{}", itemId);

        return commentRepository.findAllByItemId(itemId)
                .stream()
                .map(commentMapper::toCommentDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ItemResponseDto> searchItems(String text) {
        if (text == null || text.trim().isBlank()) {

            log.warn("По данному запросу: {} нет доступных вещей", text);

            return Collections.emptyList();
        }

        var availableItems = itemRepository.searchAvailableItems(text);

        log.info("Получен список всех доступных вещей по запросу: {}", text);

        return itemMapper.mapItemsToDtos(availableItems);
    }
}
