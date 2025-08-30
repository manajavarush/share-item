package com.project.shareitem.mapper;

import com.project.shareitem.dto.ItemCreateUpdateDto;
import com.project.shareitem.dto.ItemResponseDto;
import com.project.shareitem.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    Item toEntity(ItemCreateUpdateDto itemCreateUpdateDto);

    @Mapping(target = "ownerId", source = "owner.id")
    ItemResponseDto toResponseDto(Item item);

    default List<ItemResponseDto> mapItemsToDtos(List<Item> items) {
        if (items == null || items.isEmpty()) {
            return Collections.emptyList();
        }

        return items.stream()
                .filter(Objects::nonNull)
                .map(this::toResponseDto)
                .toList();
    }
}
