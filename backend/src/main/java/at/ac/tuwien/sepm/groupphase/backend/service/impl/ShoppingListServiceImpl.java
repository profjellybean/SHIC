package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShoppingListCreationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemStorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListItemRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ShoppingListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.lang.invoke.MethodHandles;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.List;

@Service
public class ShoppingListServiceImpl implements ShoppingListService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ShoppingListRepository shoppingListRepository;
    private final RecipeRepository recipeRepository;
    private final ItemStorageRepository itemStorageRepository;
    private final ShoppingListItemRepository shoppingListItemRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public ShoppingListServiceImpl(ShoppingListRepository shoppingListRepository,
                                   RecipeRepository recipeRepository,
                                   ItemStorageRepository itemStorageRepository,
                                   ShoppingListItemRepository shoppingListItemRepository,
                                   ItemRepository itemRepository) {
        this.recipeRepository = recipeRepository;
        this.itemStorageRepository = itemStorageRepository;
        this.shoppingListItemRepository = shoppingListItemRepository;
        this.shoppingListRepository = shoppingListRepository;
        this.itemRepository = itemRepository;
    }


    public Long createNewShoppingList(ShoppingListCreationDto dto) {
        LOGGER.debug("Creating a new shoppinglist");

        return shoppingListRepository.saveAndFlush(ShoppingList.ShoppingListBuilder.aShoppingList().withName(dto.getName()).build()).getId();
    }

    @Override
    public Long createNewShoppingList() {
        LOGGER.debug("Creating a new shopping list");
        return shoppingListRepository.saveAndFlush(new ShoppingList()).getId();
    }

    @Override
    public ShoppingList getShoppingListByid(Long id) {
        LOGGER.debug("Getting shoppinglist by id: {}", id);

        return shoppingListRepository.getById(id);
    }

    @Override
    @Transactional
    public List<ItemStorage> planRecipe(Long recipeId, Long userId) {
        LOGGER.debug("Service: plan Recipe {} based on user {}.", recipeId, userId);

        // TODO: check if user has access
        Long storageId = userId;
        Long shoppingListId = userId;

        Recipe recipe = null;
        List<ItemStorage> storageItems;
        List<ItemStorage> returnList = null;

        // TODO: catch errors that might occur accessing the repository
        try {
            recipe = recipeRepository.findRecipeById(recipeId);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("Could not find recipe with id " + recipeId, e);
        }

        try {
            storageItems = itemStorageRepository.findAllByStorageId(storageId);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("Could not find storage with id " + storageId, e);
        }

        returnList = compareItemSets(recipe.getIngredients(), storageItems);

        String notes = "Ingredient required for recipe: " + recipe.getName();
        for (ItemStorage item :
            returnList) {
            ItemStorage shoppingListItem = new ItemStorage(item);
            shoppingListItem.setShoppingListId(shoppingListId);
            shoppingListItem.setNotes(notes);
            shoppingListItem = itemStorageRepository.saveAndFlush(shoppingListItem);
            saveItem(shoppingListItem, shoppingListId);
        }
        return returnList;
    }

    /**
     * Compares two sets of items.
     * Checks which items of recipeIngredients don't appear in storedItems.
     * If they appear, the amount of the Items is compared.
     *
     * @param recipeIngredients set of items e.g. representing ingredients of a recipe
     * @param storedItems       set of items e.g. representing the stored Items in a Storage
     * @return Set of all Items that occur in recipeIngredients but not in storedItem or occur in both, but the amount in recipeIngredients is bigger than the amount in storedItems.
     */
    private List<ItemStorage> compareItemSets(Set<ItemStorage> recipeIngredients, List<ItemStorage> storedItems) {
        LOGGER.debug("Service: compareItemLists");
        List<ItemStorage> returnSet = new LinkedList<>();
        // stores items from Set into Map, because one cannot get items from a Set
        Map<String, ItemStorage> storedItemsMap = storedItems.stream().collect(Collectors.toMap(ItemStorage::getName, Function.identity()));
        for (ItemStorage ingredient :
            recipeIngredients) {
            if (!storedItems.contains(ingredient)) {
                // adds items that are not in the storage
                returnSet.add(ingredient);
            } else {
                ItemStorage storedItem = storedItemsMap.get(ingredient.getName());
                if (ingredient.getQuantity().equals(storedItem.getQuantity())) {
                    // adds items if there is not enough in the storage
                    if (ingredient.getAmount() > storedItem.getAmount()) {
                        returnSet.add(ingredient);
                    }
                } else {
                    // TODO recalculate unitOfQuantity
                }
            }
        }
        return returnSet;
    }

    @Override
    public ItemStorage saveItem(ItemStorage itemStorage, Long id) {
        LOGGER.debug("save item in shopping list");

        if (findShoppingListById(id) != null) {
            shoppingListItemRepository.saveAndFlush(itemStorage);
            shoppingListItemRepository.insert(id, itemStorage.getId());
        } else {
            Long newStorage = createNewShoppingList();
            shoppingListItemRepository.saveAndFlush(itemStorage);
            shoppingListItemRepository.insert(newStorage, itemStorage.getId());
        }
        return itemStorage;

    }

    @Override
    public Long createNewShoppingList() {
        LOGGER.debug("Creating a new shopping list");
        return shoppingListRepository.saveAndFlush(new ShoppingList()).getId();
    }

    @Override
    public List<Item> findAllItems() {
        LOGGER.debug("Find all items");
        return itemRepository.findAll();
    }


    @Override
    public List<ItemStorage> findAllByShoppingListId(Long storageId) {
        LOGGER.debug("find all storage items of shopping list");
        return shoppingListItemRepository.findAllByShoppingListId(storageId);
    }

    @Override
    public Long findShoppingListById(Long id) {
        LOGGER.debug("Getting the shopping list with the id");
        if (shoppingListRepository.findById(id).isPresent()) {
            return id;
        } else {
            return null;
        }
    }

    @Override
    public List<ItemStorage> workOffShoppingList(Long shoppingListId, Long storageId, List<ItemStorage> boughtItems) {
        LOGGER.debug("Work Off Shoppinglist {}", boughtItems);

        for (ItemStorage item : boughtItems) {
            ItemStorage itemStorage = itemStorageRepository.getById(item.getId());
            if(itemStorage.getShoppingListId().equals(shoppingListId)) {
                itemStorage.setShoppingListId(null);
                itemStorage.setStorageId(storageId);
                shoppingListItemRepository.saveAndFlush(itemStorage);
            }
        }

        return boughtItems;
    }

}
