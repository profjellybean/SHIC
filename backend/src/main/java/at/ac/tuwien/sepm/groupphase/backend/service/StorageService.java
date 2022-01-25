package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.LocationClass;
import at.ac.tuwien.sepm.groupphase.backend.entity.TrashOrUsedItem;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;

import java.time.LocalDate;
import java.util.List;

public interface StorageService {

    List<ItemStorage> cookRecipe(Long recipeId, String userName, Integer numberOfPeople);

    /**
     * Delete an item in the context of Spring Security based on the id.
     *
     * @param id the id
     * @return deleted Item
     */
    ItemStorage deleteItemById(Long id);

    /**
     * Delete an item in a specific storage based on the id of the item and the storage.
     *
     * @param itemId    the id of the item to delete
     * @param storageId the id of the storage which the item is in
     * @param trash     true if item was thrown away
     * @return a Spring Security user
     */
    ItemStorage deleteItemInStorageById(Long itemId, Long storageId, boolean trash);

    /**
     * Saves an item in the storage.
     * if there is already an Item with the same name, the Items are summed up
     *
     * @param itemStorage item to save
     * @param groupId     is used to check, if blueprint for this item exists in this group
     * @return the item
     * @throws ValidationException if invalid value exists in Item
     * @throws ServiceException    if UnitOfQuantity of an ingredient and the stored Item are not compatible
     */
    ItemStorage saveItem(ItemStorage itemStorage, Long groupId);

    /**
     * Saves an item in the storage.
     * if there is already an Item with the same name, the Items are summed up
     *
     * @param itemStorage item to save
     * @param userName    is used to get id of the users current group
     * @return the item
     * @throws ValidationException if invalid value exists in Item
     * @throws ServiceException    if UnitOfQuantity of an ingredient and the stored Item are not compatible
     */
    ItemStorage saveItemByUsername(ItemStorage itemStorage, String userName);

    /**
     * Saves updated existing item in the storage (specified in the item itself).
     *
     * @param itemStorage the item to update
     * @param groupId     is used to check if the item to update is in the groups storage
     * @return the updated item
     */
    ItemStorage updateItem(ItemStorage itemStorage, Long groupId);

    /**
     * Searches for items in the storage (specified with the id) by name.
     *
     * @param itemStorage to search for
     * @return a list of the items or none
     */
    List<ItemStorage> searchItem(ItemStorage itemStorage);

    /**
     * Gets all items from the storage (specified with the id).
     *
     * @param id the id of the storage.
     * @return a list of all the items
     */
    List<ItemStorage> getAll(Long id);

    /**
     * Finds a storage by id.
     *
     * @param id the id of the storage
     * @return the id
     */
    Long findStorageById(Long id);

    /**
     * Creates an empty new staorage.
     *
     * @return the id of the new storage.
     */
    Long createNewStorage();

    /**
     * Searches for items in the storage (specified with the id) by name.
     *
     * @param id   the id of the storage
     * @param name the name of the search
     * @return a list of the items or none
     */

    List<ItemStorage> searchItemName(Long id, String name);

    /**
     * Filters itemStorages of itemStoragesAll which are not in item itemStoragesFilter.
     *
     * @param itemStoragesAll    the id of the storage
     * @param itemStoragesFilter the name of the search
     * @return a list of the items or none
     */

    List<ItemStorage> deleteItemsWhichDoNotExists(List<ItemStorage> itemStoragesAll, List<ItemStorage> itemStoragesFilter);


    /**
     * Gets all units of quantity.
     */
    List<UnitOfQuantity> getAllUnitOfQuantity(Long storageId);

    /**
     * Gets all locations.
     *
     * @return a list of all the locations
     */
    List<LocationClass> getAllLocations();

    /**
     * Find all locations by storageId.
     *
     * @return a list of all the locations with correct storageId
     */
    List<LocationClass> getAllLocationsByStorageId(Long storageId);

    /**
     * Find all locations by name.
     *
     * @return a list of all the locations with correct name
     */
    List<LocationClass> getAllLocationsByName(String name);

    /**
     * Find all locations by name and storageId.
     *
     * @return a list of all the locations with correct name and storageId
     */
    List<LocationClass> getAllLocationsByNameAndStorageId(String name, Long storageId);

    /**
     * saves a location.
     */
    void saveLocation(LocationClass locationClass);

    /**
     * deletes a location.
     */
    void deleteLocation(Long id);

    Double sumOfArticlesOfSpecificMonth(String user, LocalDate date);

    Double sumOfArticlesOfSpecificYear(String user, LocalDate date);

    List<TrashOrUsedItem> getMostThrownAwayArticles(String user);

}
