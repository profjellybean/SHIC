package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemStorageDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ItemMapper {

    ItemDto itemToItemDto(Item item);

    List<ItemDto> itemsToItemDtos(List<Item> item);

    Item itemDtoToItem(ItemDto itemDto);

}
