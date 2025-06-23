package com.project.shareitem.mapper;

import com.project.shareitem.dto.ItemCreateUpdateDto;
import com.project.shareitem.dto.ItemResponseDto;
import com.project.shareitem.entity.Item;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    Item toEntity(ItemCreateUpdateDto itemCreateUpdateDto);

    ItemResponseDto toResponseDto(Item item);
}
