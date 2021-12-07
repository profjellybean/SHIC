package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShoppingListCreationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShoppingListDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import org.springframework.web.bind.annotation.PathVariable;

public interface ShoppingListService {

    /**
     *
     * @return DTO of newly created shoppingList
     */
    Long createNewShoppingList(ShoppingListCreationDto dto);

    /**
     *
     * @param id Id of shopping list to get
     * @return shopping list by id
     */
    ShoppingList getShoppingListByid(Long id);
}
