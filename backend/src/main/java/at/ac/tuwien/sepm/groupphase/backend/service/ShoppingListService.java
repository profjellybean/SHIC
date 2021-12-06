package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;

import java.util.List;

public interface ShoppingListService {

    /**
     * insert a storage item to the shopping list
     *
     * @return inserted object
     */
    ItemStorage saveItem(ItemStorage itemStorage, Long id);

    /**
     * finds all storage items in a shopping list
     *
     * @return returns list of storage items
     */
    List<ItemStorage> findAllByStorageId(Long storageId);

    /**
     * checks if shoppinglist exists
     *
     * @return returns id of the shopping list
     */
    Long findShoppingListById(Long id);

    /**
     * creates new shoppinglist if not existing
     *
     * @return returns id of the shopping list
     */
    Long createNewShoppingList();

}
