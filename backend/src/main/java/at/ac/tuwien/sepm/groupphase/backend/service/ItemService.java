package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitsRelation;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;

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
     *
     * @throws ValidationException if groupId is null
     */
    List<Item> getAllItemsForGroup(Long groupId);

    /**
     * find all items by specified groupId.
     *
     * @param groupId id of the group
     *
     * @return all items with specified groupId
     *
     * @throws ValidationException if groupId is null
     */
    List<Item> findAllByGroupId(Long groupId);

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
     * If not it adds a blueprint to the table for the group.
     *
     * @param groupId id of the group
     * @param itemStorage item to check
     *
     * @return item that was checked
     *
     * @throws ValidationException if invalid value exists in item
     */
    ItemStorage checkForBluePrintForGroup(ItemStorage itemStorage, Long groupId);

    /**
     * edits a custom item of a group.
     * groupId has to be set.
     *
     * @param item that replaces stored item.
     *
     * @return edited item.
     *
     * @throws ValidationException if invalid value exists in item
     */
    Item editCustomItem(Item item);

    /**
     * saves a custom item of a group.
     * groupId has to be set.
     *
     * @param item that is saved.
     *
     * @return saved item.
     *
     * @throws ValidationException if invalid value exists in item
     */
    Item addCustomItem(Item item);

    /**
     * Finds all UnitsOfQuantity in database for specific Group with those without group.
     *
     * @param groupId that is saved.
     *
     * @return unitsOfQuantities
     */
    List<UnitOfQuantity> getAllForGroup(Long groupId);
}
