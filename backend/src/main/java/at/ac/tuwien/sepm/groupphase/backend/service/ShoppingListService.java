package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShoppingListCreationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;

import java.util.List;

public interface ShoppingListService {

    /**
     * Creates a new Shoppinglist.
     *
     * @return DTO of newly created shoppingList
     */
    Long createNewShoppingList(ShoppingListCreationDto dto);

    /**
     * creates new shoppinglist if not existing.
     *
     * @return returns id of the shopping list
     */
    Long createNewShoppingList();

    /**
     * Gets the shopping list by id.
     *
     * @param id Id of shopping list to get
     * @return shopping list by id
     */
    ShoppingList getShoppingListByid(Long id);

    /**
     * plans a recipe. Checks which of the required ingredients are not present
     * and sets them on the shopping list.
     *
     * @param recipeId  id of recipe that user wants to cook
     * @param storageId id of storage of the group, of which the user is part of
     * @return a List of all ingredients that were added to the ShoppingList
     */
    List<ItemStorage> planRecipe(Long recipeId, Long storageId);

    /**
     * Insert a storage item to the shopping list.
     *
     * @return inserted object
     */
    ItemStorage saveItem(ItemStorage itemStorage, Long id);

    /**
     * Finds all storage items in a shopping list.
     *
     * @return returns list of storage items
     */
    List<ItemStorage> findAllByShoppingListId(Long storageId);

    /**
     * Checks if shoppinglist exists.
     *
     * @return returns id of the shopping list
     */
    Long findShoppingListById(Long id);

    /**
     * Find all item entries.
     *
     * @return ordered list of all item entries
     */
    List<Item> findAllItems();

    List<ItemStorage> workOffShoppingList(String username, List<ItemStorage> boughtItems);
}
