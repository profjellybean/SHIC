package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;

import java.util.List;

public interface ItemService {
    UnitOfQuantity addUnitOfQuantity(UnitOfQuantity unitOfQuantity);

    List<UnitOfQuantity> getAll();

    /**
     * Deletes the item from the database.
     *
     * @param itemDto the item to delete
     * @return true if successful
     */
    boolean delete(Item itemDto);
}
