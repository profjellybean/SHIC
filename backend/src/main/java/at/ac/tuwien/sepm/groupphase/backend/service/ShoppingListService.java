package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShoppingListCreationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
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
     * Plans a recipe.
     * Checks which of the required ingredients are not present
     * and puts them on the public shopping-list, with the amount that is missing from the storage.
     *
     * @param recipeId  id of recipe that user wants to plan
     * @param userName of user who sent the request
     * @param numberOfPeople number of people the Recipe is planned for
     *
     * @return a List of all the ingredients that were added to the ShoppingList
     *
     * @throws ValidationException if the recipe or values in User are invalid
     * @throws NotFoundException if the recipe or the items in storage can not be found
     * @throws ServiceException if UnitOfQuantity of an ingredient and the stored Item are not compatible
     */
    List<ItemStorage> planRecipe(Long recipeId, String userName, Integer numberOfPeople);

    /**
     * puts all ingredients of a Recipe on the Shoppinglist.
     * regardless of what`s in the Storage
     *
     * @param recipeId  id of recipe that user wants to cook
     * @param userName of user who sent the request
     * @param numberOfPeople number of people the Recipe is planned for
     *
     * @return a List of all the ingredients that were added to the ShoppingList
     *
     * @throws ValidationException if the recipe or values in User are invalid
     * @throws NotFoundException if the recipe or the items in storage can not be found
     * @throws ServiceException if UnitOfQuantity of an ingredient and the stored Item are not compatible
     */
    List<ItemStorage> putRecipeOnShoppingList(Long recipeId, String userName, Integer numberOfPeople);

    /**
     * Insert a storage item to the shopping list.
     *
     * @param groupId is used to check if blueprint for this item exists in this group.
     *
     * @return inserted object
     */
    ItemStorage saveItem(ItemStorage itemStorage, Long id, Long groupId);

    /**
     * Change amount of a storage item of the shopping list.
     *
     * @param itemStorage item with changed amount
     *
     * @return inserted object
     */
    ItemStorage changeAmountOfItem(ItemStorage itemStorage, Long shoppingListId);

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
     * search itemStorage items with matching parameter.
     *
     * @param itemStorage itemStorage with parameters
     * @return result of the search
     */
    List<ItemStorage> searchItem(ItemStorage itemStorage);

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


    void deleteItemById(Long itemId, Long shoppingListId);


}
