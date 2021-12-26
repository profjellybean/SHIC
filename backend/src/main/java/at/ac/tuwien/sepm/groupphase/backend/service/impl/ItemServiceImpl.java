package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitsRelation;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UnitOfQuantityRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UnitsRelationRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UnitOfQuantityRepository unitOfQuantityRepository;
    private final ItemRepository itemRepository;
    private final UnitsRelationRepository unitsRelationRepository;

    @Autowired
    public ItemServiceImpl(UnitOfQuantityRepository unitOfQuantityRepository, ItemRepository itemRepository, UnitsRelationRepository unitsRelationRepository) {
        this.unitOfQuantityRepository = unitOfQuantityRepository;
        this.itemRepository = itemRepository;
        this.unitsRelationRepository = unitsRelationRepository;
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
    public String getUnitOfQuantityById(Long id) {
        LOGGER.debug("Find Unit of Quantity by id: {}", id);
        return unitOfQuantityRepository.getUnitOfQuantityById(id).getName();
    }

    @Override
    public List<Item> getAllItems() {
        LOGGER.debug("Getting all items");
        return itemRepository.findAll();
    }

    @Override
    public List<Item> getAllItemsForGroup(Long groupId) {
        LOGGER.debug("Getting all items for group {}", groupId);
        return itemRepository.findAllItemsForGroup(groupId);
    }

    @Override
    public List<Item> findAllByGroupId(Long groupId) {
        LOGGER.debug("Getting all items by groupId {}", groupId);
        if (groupId == null) {
            throw new ValidationException("groupId can not be null");
        }
        return itemRepository.findAllByGroupId(groupId);
    }

    @Override
    public ItemStorage checkForBluePrintForGroup(ItemStorage itemStorage, Long groupId) {
        LOGGER.debug("Service: Check for Item blueprint {}", itemStorage);

        if (itemStorage == null) {
            throw new ValidationException("itemStorage can not be null");
        }
        if (groupId == null) {
            return null;
        }

        List<Item> items = itemRepository.findItemsByNameForGroup(itemStorage.getName(), groupId);
        if (items.isEmpty()) {
            Item newBlueprint = new Item(null, itemStorage.getName(), itemStorage.getQuantity(), groupId);
            itemRepository.saveAndFlush(newBlueprint);
        }
        return itemStorage;
    }

    @Override
    public Item editCustomItem(Item item) {
        LOGGER.debug("Service: Edit Item {}", item);

        if (item == null) {
            throw new ValidationException("item can not be null when editing");
        }
        return itemRepository.saveAndFlush(item);
    }


}
