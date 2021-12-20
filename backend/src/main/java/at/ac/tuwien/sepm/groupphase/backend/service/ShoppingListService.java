package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShoppingListCreationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
//import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.core.Authentication;

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
     * @param authentication of user who sent the request
     * @return a List of all ingredients that were added to the ShoppingList
     */
    List<ItemStorage> planRecipe(Long recipeId, Authentication authentication);

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

    /**
     * Work off shoppinglist. A list of items  will be transfered from shoppinglist to storage.
     *
     * @param authentication of the logged in user
     * @param boughtItems the items which have been bought and will be transfered into the storage
     * @return a list of items which were tranfered into the storage
     */
    List<ItemStorage> workOffShoppingList(Authentication authentication, List<ItemStorage> boughtItems);
}
