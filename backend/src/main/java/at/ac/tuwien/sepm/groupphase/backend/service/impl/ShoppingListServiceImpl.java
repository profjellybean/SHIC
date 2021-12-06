package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListItemRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ShoppingListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class ShoppingListServiceImpl implements ShoppingListService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ShoppingListRepository shoppingListRepository;
    private final ShoppingListItemRepository shoppingListItemRepository;

    @Autowired
    public ShoppingListServiceImpl(ShoppingListRepository shoppingListRepository, ShoppingListItemRepository shoppingListItemRepository) {
        this.shoppingListRepository = shoppingListRepository;
        this.shoppingListItemRepository = shoppingListItemRepository;
    }

    @Override
    public ItemStorage saveItem(ItemStorage itemStorage, Long id) {
        LOGGER.debug("save item in shopping list");

        if (findShoppingListById(id) != null) {
            shoppingListItemRepository.saveAndFlush(itemStorage);
            shoppingListItemRepository.insert(itemStorage.getId(), id);
        } else {
            Long newStorage = createNewShoppingList();
            shoppingListItemRepository.saveAndFlush(itemStorage);
            shoppingListItemRepository.insert(itemStorage.getId(), newStorage);
        }
        return itemStorage;

    }

    @Override
    public Long createNewShoppingList() {
        LOGGER.debug("Creating a new shopping list");
        return shoppingListRepository.saveAndFlush(new ShoppingList()).getId();
    }


    @Override
    public List<ItemStorage> findAllByStorageId(Long storageId) {
        LOGGER.debug("find all storage items of shopping list");
        return shoppingListItemRepository.findAllByStorageId(storageId);
    }

    @Override
    public Long findShoppingListById(Long id) {
        LOGGER.debug("Getting the shopping list with the id");
        if(shoppingListRepository.findById(id).isPresent()){
            return id;
        }
        else {
            return null;
        }
    }
}
