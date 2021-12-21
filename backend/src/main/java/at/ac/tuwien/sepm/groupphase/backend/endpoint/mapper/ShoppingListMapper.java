package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemStorageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShoppingListDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ShoppingListMapper {

    ShoppingListDto shoppingListToShoppingListDto(ShoppingList shoppingList);

    List<ShoppingListDto> shoppingListToShoppingListDto(List<ShoppingList> shoppingList);

    ShoppingList shoppingListDtoToShoppingList(ShoppingListDto shoppingList);

    List<ShoppingList> shoppingListDtoToShoppingList(List<ShoppingListDto> shoppingList);


}
