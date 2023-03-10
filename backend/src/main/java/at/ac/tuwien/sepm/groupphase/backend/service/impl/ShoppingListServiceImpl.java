package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShoppingListCreationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitsRelation;
import at.ac.tuwien.sepm.groupphase.backend.entity.UserGroup;
import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.Location;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemStorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListItemRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UnitsRelationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ItemService;
import at.ac.tuwien.sepm.groupphase.backend.service.ShoppingListService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.util.ShoppingListValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ShoppingListServiceImpl implements ShoppingListService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ShoppingListValidator shoppingListValidator;
    private final ShoppingListRepository shoppingListRepository;
    private final RecipeRepository recipeRepository;
    private final ItemStorageRepository itemStorageRepository;
    private final ShoppingListItemRepository shoppingListItemRepository;
    private final ItemRepository itemRepository;
    private final UnitsRelationRepository unitsRelationRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ItemService itemService;

    @Autowired
    public ShoppingListServiceImpl(ShoppingListRepository shoppingListRepository,
                                   ShoppingListValidator shoppingListValidator, RecipeRepository recipeRepository,
                                   ItemStorageRepository itemStorageRepository,
                                   ShoppingListItemRepository shoppingListItemRepository,
                                   ItemRepository itemRepository, UnitsRelationRepository unitsRelationRepository,
                                   UserRepository userRepository, UserService userService,
                                   ItemService itemService) {
        this.shoppingListValidator = shoppingListValidator;
        this.recipeRepository = recipeRepository;
        this.itemStorageRepository = itemStorageRepository;
        this.shoppingListItemRepository = shoppingListItemRepository;
        this.shoppingListRepository = shoppingListRepository;
        this.unitsRelationRepository = unitsRelationRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.itemService = itemService;
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
    public List<ItemStorage> searchItem(ItemStorage itemStorage) {
        LOGGER.info("Search for Items by ItemStorage {}", itemStorage);
        if (itemStorage.getNotes() != null) {
            if (itemStorage.getNotes().trim().equals("")) {
                itemStorage.setNotes(null);
            }
        }
        if (itemStorage.getName() != null) {
            if (itemStorage.getName().trim().equals("")) {
                itemStorage.setName(null);
            }
        }
        if (itemStorage.getLocationTag() != null) {
            if (itemStorage.getLocationTag().trim().equals("")) {
                itemStorage.setLocationTag(null);
            }
        }

        return itemStorageRepository.findAllByItemStorageForShoppingList(
            itemStorage.getShoppingListId(),
            itemStorage.getAmount(),
            itemStorage.getLocationTag() == null ? null : itemStorage.getLocationTag(),
            itemStorage.getName() == null ? null : "%" + itemStorage.getName() + "%",
            itemStorage.getNotes() == null ? null : "%" + itemStorage.getNotes() + "%",
            itemStorage.getExpDate());

    }

    @Override
    @Transactional
    public List<ItemStorage> planRecipe(Long recipeId, String userName, Integer numberOfPeople) {
        LOGGER.debug("Service: plan Recipe {} for {} people based on user {}.", recipeId, numberOfPeople, userName);

        shoppingListValidator.validate_planRecipe(recipeId, userName, numberOfPeople);

        Recipe recipe = recipeRepository.findRecipeById(recipeId);
        Set<ItemStorage> calculatedIngredients = new HashSet<>();
        for (ItemStorage item :
            recipe.getIngredients()) {
            ItemStorage calculatedItem = new ItemStorage(item);
            calculatedItem.setAmount(item.getAmount() * numberOfPeople);
            calculatedIngredients.add(calculatedItem);
        }

        Long storageId = userService.loadGroupStorageByUsername(userName);
        List<ItemStorage> storageItems = itemStorageRepository.findAllByStorageId(storageId);
        List<ItemStorage> returnList = compareItemSets(calculatedIngredients, storageItems);

        Long shoppingListId = userService.getPublicShoppingListIdByUsername(userName);
        String notes = "Ingredient for recipe: " + recipe.getName();
        for (ItemStorage item :
            returnList) {
            item.setNotes(notes);
            saveItem(item, shoppingListId, null);
        }
        return returnList;
    }

    /**
     * Compares two sets of items.
     * Checks which items of recipeIngredients don't appear in storedItems.
     * If they appear, the amount of the Items is compared and adjusted so that the amount of returnSet + storedItem = recipeIngredient
     *
     * @param recipeIngredients set of items e.g. representing ingredients of a recipe
     * @param storedItems       set of items e.g. representing the stored Items in a Storage.
     * @return List of all Items that occur in recipeIngredients but not in storedItem, or occur in both, but the amount in recipeIngredients is bigger than the amount in storedItems.
     */
    private List<ItemStorage> compareItemSets(Set<ItemStorage> recipeIngredients, List<ItemStorage> storedItems) {
        LOGGER.debug("Service: compareItemLists");
        List<ItemStorage> returnSet = new LinkedList<>();
        // stores items from Set into Map, because one cannot get items from a Set
        Map<String, ItemStorage> storedItemsMap = storedItems.stream().collect(Collectors.toMap(ItemStorage::getName, Function.identity()));
        for (ItemStorage ingredient :
            recipeIngredients) {
            ItemStorage storedItem = storedItemsMap.get(ingredient.getName());
            if (storedItem == null) {
                // adds items that are not in the storage
                returnSet.add(ingredient);
            } else {
                if (ingredient.getQuantity().equals(storedItem.getQuantity())) {
                    // adds items if there is not enough of them in the storage
                    if (ingredient.getAmount() > storedItem.getAmount()) {
                        int amount = ingredient.getAmount() - storedItem.getAmount();
                        ingredient.setAmount(amount);
                        returnSet.add(ingredient);
                    }
                } else {
                    UnitsRelation unitsRelation = unitsRelationRepository.findUnitsRelationByBaseUnitAndCalculatedUnit(
                        ingredient.getQuantity().getName(), storedItem.getQuantity().getName());
                    if (unitsRelation != null) {
                        Double relation = unitsRelation.getRelation();
                        if (ingredient.getAmount() * relation > storedItem.getAmount()) {
                            int amount = (int) (ingredient.getAmount() * relation - storedItem.getAmount());
                            ingredient.setAmount(amount);
                            returnSet.add(ingredient);
                        }
                    }
                }
            }
        }
        return returnSet;
    }

    @Override
    @Transactional
    public List<ItemStorage> putRecipeOnShoppingList(Long recipeId, String userName, Integer numberOfPeople) {
        LOGGER.debug("Service: put all Recipe-Ingredients {} on ShoppingList for {} people based on user {}.", recipeId, numberOfPeople, userName);

        // validation
        shoppingListValidator.validate_putRecipeOnShoppingList(recipeId, userName, numberOfPeople);

        Recipe recipe = recipeRepository.findRecipeById(recipeId);
        Long shoppingListId = userService.getPublicShoppingListIdByUsername(userName);

        List<ItemStorage> returnList = new ArrayList<>();
        String notes = "Ingredient for recipe: " + recipe.getName();
        for (ItemStorage item :
            recipe.getIngredients()) {
            ItemStorage itemToSave = new ItemStorage(item);
            itemToSave.setNotes(notes);
            itemToSave.setAmount(item.getAmount() * numberOfPeople);
            saveItem(itemToSave, shoppingListId, null);
            returnList.add(itemToSave);
        }
        return returnList;
    }


    @Override
    public ItemStorage saveItem(ItemStorage itemStorage, Long shoppingListId, Long groupId) {
        LOGGER.debug("save item in shopping list");

        if (itemStorage.getLocationTag() != null) {
            try {
                Location.valueOf(itemStorage.getLocationTag());
            } catch (IllegalArgumentException i) {
                throw new ValidationException("Location is not valid");
            }
        }

        if (groupId != null) {
            itemService.checkForBluePrintForGroup(itemStorage, groupId);
        }

        // check if there is already an item with the same name in the shoppinglist
        if (shoppingListId != null) {
            List<ItemStorage> itemsInShoppingList = itemStorageRepository
                .findAllByShoppingListId(shoppingListId);
            Map<String, ItemStorage> storedItemsMap = itemsInShoppingList.stream()
                .collect(Collectors.toMap(ItemStorage::getName, Function.identity()));
            ItemStorage storedItem = storedItemsMap.get(itemStorage.getName());
            // if there is uch an item, compare the units of quantity
            if (storedItem != null && storedItem.getQuantity().equals(itemStorage.getQuantity())) {
                // if they are the same, add the amounts and save
                int newAmount = storedItem.getAmount() + itemStorage.getAmount();
                storedItem.setAmount(newAmount);
                itemStorageRepository.saveAndFlush(storedItem);
                return null;
                /// In this case null is returned so that the frontend can differentiate between "Item was added to the list" and "Item in the list was changed".
                /// This is to tell the frontend whether it should reload or not !!
            } else if (storedItem != null && storedItem.getQuantity() != null) {
                // else recalculate the amount, then add the amounts and save
                if (itemStorage.getQuantity() == null) {
                    throw new ValidationException("Unit of Quantity has to be set");
                }
                UnitsRelation unitsRelation = unitsRelationRepository.findUnitsRelationByBaseUnitAndCalculatedUnit(
                    storedItem.getQuantity().getName(), itemStorage.getQuantity().getName());
                if (unitsRelation != null) {
                    int newAmount = (int) (storedItem.getAmount() * unitsRelation.getRelation() + itemStorage.getAmount());
                    storedItem.setAmount(newAmount);
                    storedItem.setQuantity(itemStorage.getQuantity());
                    return itemStorageRepository.saveAndFlush(storedItem);
                } else {
                    throw new ServiceException("Incompatible types: Same item with different unit of quantity found");
                }
            }
        }

        itemStorage.setShoppingListId(shoppingListId);
        itemStorage = shoppingListItemRepository.saveAndFlush(itemStorage);
        shoppingListItemRepository.insert(shoppingListId, itemStorage.getId());

        return itemStorage;

    }

    @Override
    public ItemStorage changeAmountOfItem(ItemStorage itemStorage, Long shoppingListId) {
        LOGGER.debug("change amount of item in shopping list");
        if (itemStorage.getShoppingListId() != null && itemStorage.getShoppingListId().equals(shoppingListId)) {
            return shoppingListItemRepository.saveAndFlush(itemStorage);
        } else {
            throw new ServiceException("This item is not on the given shopping list");
        }
    }

    @Transactional
    @Override
    public List<Item> findAllItems() {
        LOGGER.debug("Find all items");
        return itemRepository.findAllByOrderByNameAsc();
    }

    @Override
    public List<ItemStorage> findAllByShoppingListId(Long storageId) {
        LOGGER.debug("find all storage items of shopping list");
        return shoppingListItemRepository.findAllByShoppingListIdOrderByNameAsc(storageId);
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
    public List<ItemStorage> workOffShoppingList(Authentication authentication, List<ItemStorage> boughtItems) {
        LOGGER.debug("Work Off Shoppinglist {}", boughtItems);

        List<ItemStorage> storedItems = new LinkedList<>();
        Optional<ApplicationUser> userOptional = userRepository.findUserByUsername(authentication.getName());
        if (userOptional.isPresent()) {
            ApplicationUser user = userOptional.get();
            UserGroup group = user.getCurrGroup();
            Long storageId = group.getStorageId();

            List<ItemStorage> allItemsInStorage = itemStorageRepository.findAllByStorageId(storageId);
            Map<String, ItemStorage> itemStorageMap = allItemsInStorage.stream()
                .collect(Collectors.toMap(ItemStorage::getName, Function.identity()));

            for (ItemStorage item : boughtItems) {
                ItemStorage storageItem = itemStorageMap.get(item.getName());
                if (storageItem != null && storageItem.getQuantity().equals(item.getQuantity())) {
                    int newAmount = storageItem.getAmount() + item.getAmount();
                    storageItem.setAmount(newAmount);
                    Long shoppingListId = item.getShoppingListId();
                    shoppingListItemRepository.deleteFromTable(shoppingListId, item.getId());
                    itemStorageRepository.saveAndFlush(storageItem);
                    storedItems.add(item);
                } else if (storageItem != null && storageItem.getQuantity() != null) {
                    UnitsRelation unitsRelation = unitsRelationRepository.findUnitsRelationByBaseUnitAndCalculatedUnit(
                        storageItem.getQuantity().getName(), item.getQuantity().getName());
                    if (unitsRelation != null) {
                        double relation = unitsRelation.getRelation();
                        int newAmount = (int) (storageItem.getAmount() * relation + item.getAmount());
                        storageItem.setAmount(newAmount);
                        storageItem.setQuantity(item.getQuantity());
                        Long shoppingListId = item.getShoppingListId();
                        shoppingListItemRepository.deleteFromTable(shoppingListId, item.getId());
                        itemStorageRepository.saveAndFlush(storageItem);
                        storedItems.add(item);
                    } else {
                        throw new ServiceException("Incompatible units of quantity");
                    }
                } else {

                    Long shoppingListId = item.getShoppingListId();
                    item.setShoppingListId(null);
                    item.setStorageId(storageId);

                    shoppingListItemRepository.deleteFromTable(shoppingListId, item.getId());
                    itemStorageRepository.saveAndFlush(item);
                    storedItems.add(item);
                }
            }
        }
        return storedItems;
    }

    @Override
    public void deleteItemById(Long itemId, Long shoppingListId) {
        int rowsDeleted = shoppingListItemRepository.deleteFromTable(shoppingListId, itemId);
        itemStorageRepository.deleteById(itemId);
        if (rowsDeleted == 0) {
            throw new NotFoundException("Item not found");
        }
        LOGGER.debug("::: " + rowsDeleted);

    }


}
