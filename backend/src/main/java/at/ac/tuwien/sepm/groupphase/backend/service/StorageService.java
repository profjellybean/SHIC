package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;

import java.util.List;

public interface StorageService {
    /**
     * Delete an item in the context of Spring Security based on the id.
     *
     * @param id the id
     * @return a Spring Security user
     */
    ItemStorage deleteItemById(Long id);

    /**
     * Saves an item in the storage (specified in the item itself).
     *
     * @param groupId is used to check if blueprint for this item exists in this group.@param itemStorage the item
     *
     * @return the item
     */
    ItemStorage saveItem(ItemStorage itemStorage, Long groupId);

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
     * @param itemStoragesAll the id of the storage
     * @param itemStoragesFilter the name of the search
     * @return a list of the items or none
     */

    List<ItemStorage> deleteItemsWhichDoNotExists(List<ItemStorage> itemStoragesAll, List<ItemStorage> itemStoragesFilter);


    /**
     * Gets all units of quantity.
     */
    List<UnitOfQuantity> getAllUnitOfQuantity();
}
