package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;

import java.util.List;

public interface StorageService {
    /**
     * Delete an item in the context of Spring Security based on the id
     *
     * @param id the id
     * @return a Spring Security user
     */

    ItemStorage deleteItemById(Long id);

    ItemStorage saveItem(ItemStorage itemStorage);

    List<ItemStorage> searchItem(String name);

    List<ItemStorage> getAll(Long id);

    Long findStorageById(Long id);

    Long createNewStorage();
}
