package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShoppingListDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ItemMapper {
    ItemDto itemToItemDto(Item item);

    List<ItemDto> itemsToItemsDto(List<Item> itemStorages);

    Item itemDtoToItem(ItemDto itemDto);

    List<Item> itemsDtoToItems(List<ItemDto> itemDto);
}
