package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import org.mapstruct.Mapper;

@Mapper
public interface ItemMapper {
    Item itemDtoToItem(ItemDto itemDto);

    ItemDto itemToItemDto(Item item);
}
