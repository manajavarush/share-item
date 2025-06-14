package com.project.shareitem.mapper;

import com.project.shareitem.dto.ItemRequestDto;
import com.project.shareitem.dto.ItemResponseDto;
import com.project.shareitem.model.Item;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    Item itemRequestDtoToItem(ItemRequestDto itemRequestDto);

    ItemResponseDto itemToItemResponseDto(Item item, int rentCount);
}
