package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UnitOfQuantityRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ItemService;
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

    @Autowired
    public ItemServiceImpl(UnitOfQuantityRepository unitOfQuantityRepository, ItemRepository itemRepository) {
        this.unitOfQuantityRepository = unitOfQuantityRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public UnitOfQuantity addUnitOfQuantity(UnitOfQuantity unitOfQuantity) {
        LOGGER.debug("Save unitOfQuantity");
        unitOfQuantityRepository.save(unitOfQuantity);
        return unitOfQuantity;
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
}
