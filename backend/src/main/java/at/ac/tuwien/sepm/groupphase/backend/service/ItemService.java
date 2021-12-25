package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
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

    List<Item> getAllItems();

    /**
     * gets all items for a group. where groupId is either null or the id of the group.
     *
     * @param groupId id of the group
     *
     * @return list of said items
     */
    List<Item> getAllItemsForGroup(Long groupId);

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
     * Finds specific Relation between BaseUnit and CalculatedUnit.
     *
     * @param baseUnit and calculatedUnit
     * @return unitsRelations
     */
    UnitsRelation getSpecificRelation(String baseUnit, String calculatedUnit);

    /**
     * Deletes the item from the database.
     *
     * @param itemDto the item to delete
     * @return true if successful
     */
    boolean delete(Item itemDto);

    /**
     * get unit of quantity by id from the database.
     *
     * @param id the unitOfQuantity
     * @return unitOfQuantity name
     */
    String getUnitOfQuantityById(Long id);

    /**
     * checks if a blueprint of this Item already exists for the group in table ITEM.
     * If not it adds a blueprint to the table.
     *
     * @param itemStorage item to check
     */
    ItemStorage checkForBluePrintForGroup(ItemStorage itemStorage, Long groupId);
}
