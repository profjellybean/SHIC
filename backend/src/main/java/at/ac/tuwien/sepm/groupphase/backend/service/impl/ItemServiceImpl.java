package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitsRelation;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UnitOfQuantityRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UnitsRelationRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ItemService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.util.ItemValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UnitOfQuantityRepository unitOfQuantityRepository;
    private final ItemRepository itemRepository;
    private final UnitsRelationRepository unitsRelationRepository;
    private final UserService userService;
    private final ItemValidator itemValidator;

    @Autowired
    public ItemServiceImpl(UnitOfQuantityRepository unitOfQuantityRepository, ItemRepository itemRepository,
                           UnitsRelationRepository unitsRelationRepository, UserService userService, ItemValidator itemValidator) {
        this.unitOfQuantityRepository = unitOfQuantityRepository;
        this.itemRepository = itemRepository;
        this.unitsRelationRepository = unitsRelationRepository;
        this.userService = userService;
        this.itemValidator = itemValidator;
    }

    @Override
    public UnitOfQuantity addUnitOfQuantity(UnitOfQuantity unitOfQuantity) {
        LOGGER.debug("Save unitOfQuanity");
        unitOfQuantityRepository.save(unitOfQuantity);
        return unitOfQuantity;
    }

    @Override
    public UnitsRelation addUnitsRelation(UnitsRelation unitsRelation) {
        LOGGER.debug("Save Units Relation");
        UnitsRelation unitsRelation1 = new UnitsRelation();
        unitsRelation1.setRelation(1 / unitsRelation.getRelation());
        unitsRelation1.setBaseUnit(unitsRelation.getCalculatedUnit());
        unitsRelation1.setCalculatedUnit(unitsRelation.getBaseUnit());
        if (unitsRelationRepository.findUnitsRelationByBaseUnitAndCalculatedUnit(unitsRelation1.getBaseUnit(), unitsRelation1.getCalculatedUnit()) != null) {
            return null;
        }
        if (unitsRelationRepository.findUnitsRelationByBaseUnitAndCalculatedUnit(unitsRelation.getBaseUnit(), unitsRelation.getCalculatedUnit()) != null) {
            return null;
        }
        unitsRelationRepository.save(unitsRelation);
        unitsRelationRepository.save(unitsRelation1);
        return unitsRelation1;
    }

    @Override
    public List<UnitsRelation> getAllUnitsRelations() {
        LOGGER.debug("Getting all UnitsRelations");
        return unitsRelationRepository.findAll();
    }

    @Override
    public UnitsRelation getSpecificRelation(String baseUnit, String calculatedUnit) {
        LOGGER.debug("get specific Relation between two units {} and {}", baseUnit, calculatedUnit);
        return unitsRelationRepository.findUnitsRelationByBaseUnitAndCalculatedUnit(baseUnit, calculatedUnit);
    }

    @Override
    public List<UnitOfQuantity> getAllForGroup(Long groupId) {
        LOGGER.debug("Getting all units of quantity");
        return unitOfQuantityRepository.findAllByGroupIdOrGroupIdIsNull(groupId);
    }


    @Override
    public List<UnitOfQuantity> getAll() {
        LOGGER.debug("Getting all units of quantity");
        return unitOfQuantityRepository.findAll();
    }

    @Override
    public boolean delete(Item item) {
        LOGGER.debug("Delete item {}", item.getName());
        itemRepository.delete(item);
        return !itemRepository.existsById(item.getId());
    }

    @Override
    public List<Item> searchByName(String name, String userName) {
        LOGGER.debug("Get items by name {}", name);
        String partName = name;
        if (userName == null) {
            throw new ValidationException("User can not be null");
        }
        Long groupId = userService.getGroupIdByUsername(userName);
        if (groupId == null) {
            throw new ValidationException("groupId can not be null");
        }
        if (name == null || name == "") {
            return this.getAllItemsForGroupByUsername(userName);
        } else {
            partName = "%" + name + "%";
            return itemRepository.findAllItemsByNameForGroup(partName, groupId);
        }
    }

    @Override
    public String getUnitOfQuantityById(Long id) {
        LOGGER.debug("Find Unit of Quantity by id: {}", id);
        return unitOfQuantityRepository.getUnitOfQuantityById(id).getName();
    }

    @Override
    public List<Item> getAllItems() {
        LOGGER.debug("Service: Getting all items");
        return itemRepository.findAllByOrderByNameAsc();
    }

    @Override
    public List<Item> getAllItemsForGroupByUsername(String userName) {
        LOGGER.debug("Service: Getting all items for group of user {}", userName);
        if (userName == null) {
            throw new ValidationException("User can not be null");
        }
        Long groupId = userService.getGroupIdByUsername(userName);
        if (groupId == null) {
            throw new ValidationException("groupId can not be null");
        }
        return itemRepository.findAllItemsForGroupOrderByNameAsc(groupId);
    }

    @Override
    public List<Item> findAllByGroupIdByUsername(String userName) {
        LOGGER.debug("Service: Getting all items by groupId of user {}", userName);
        if (userName == null) {
            throw new ValidationException("User can not be null");
        }
        Long groupId = userService.getGroupIdByUsername(userName);
        if (groupId == null) {
            throw new ValidationException("groupId can not be null");
        }
        return itemRepository.findAllByGroupIdOrderByNameAsc(groupId);
    }

    @Override
    public ItemStorage checkForBluePrintForGroup(ItemStorage itemStorage, Long groupId) {
        LOGGER.debug("Service: Check for Item blueprint {} for group {}", itemStorage, groupId);

        if (itemStorage == null) {
            throw new ValidationException("itemStorage can not be null");
        }
        if (groupId == null) {
            return null;
        }

        List<Item> items = itemRepository.findItemsByNameForGroup(itemStorage.getName(), groupId);
        if (!items.isEmpty()) {
            return itemStorage;
        }

        Item newBlueprint = new Item(null, itemStorage.getName(), itemStorage.getQuantity(), groupId);
        itemRepository.saveAndFlush(newBlueprint);
        return itemStorage;
    }

    @Override
    public Item editCustomItem(Item item, String userName) {
        LOGGER.debug("Service: Edit Item {} for User {}", item, userName);

        itemValidator.validate_saveCustomItem(item, userName);

        Long groupId = userService.getGroupIdByUsername(userName);
        item.setGroupId(groupId);

        List<Item> items = itemRepository.findItemsByNameForGroup(item.getName(), item.getGroupId());
        if (!items.isEmpty() && !items.get(0).getId().equals(item.getId())) {
            throw new ValidationException("Item with same Name already exists");
        }
        return itemRepository.saveAndFlush(item);
    }

    @Override
    public Item addCustomItem(Item item, String userName) {
        LOGGER.debug("Service: Add Item {} for User {}", item, userName);

        itemValidator.validate_saveCustomItem(item, userName);

        Long groupId = userService.getGroupIdByUsername(userName);
        item.setGroupId(groupId);

        List<Item> items = itemRepository.findItemsByNameForGroup(item.getName(), item.getGroupId());
        if (!items.isEmpty()) {
            throw new ValidationException("Item with same Name already exists");
        }
        return itemRepository.saveAndFlush(item);
    }


}
