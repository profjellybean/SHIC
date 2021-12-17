package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitsRelation;

import java.util.List;

public interface ItemService {
    /**
     * Adds UnitOfQuantity in database.
     *
     * @param unitOfQuantity the item to save
     * @return unitOfQuantity
     */
    UnitOfQuantity addUnitOfQuantity(UnitOfQuantity unitOfQuantity);

    /**
     * Finds all UnitsOfQuantity in database.
     *
     * @return unitsOfQuantities
     */
    List<UnitOfQuantity> getAll();

    /**
     * Adds UnitsRelation in database.
     *
     * @param unitsRelation the item to save
     * @return unitsRelation
     */
    UnitsRelation addUnitsRelation(UnitsRelation unitsRelation);

    /**
     * Finds all UnitsRelations in database.
     *
     * @return unitsRelations
     */
    List<UnitsRelation> getAllUnitsRelations();

    /**
     * Deletes the item from the database.
     *
     * @param itemDto the item to delete
     * @return true if successful
     */
    boolean delete(Item itemDto);
}
