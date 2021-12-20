package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShoppingListCreationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.UserGroup;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitsRelation;
import at.ac.tuwien.sepm.groupphase.backend.entity.UserGroup;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UnitsRelationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemStorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListItemRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ShoppingListService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.hibernate.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.lang.invoke.MethodHandles;
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
    private final ShoppingListRepository shoppingListRepository;
    private final RecipeRepository recipeRepository;
    private final ItemStorageRepository itemStorageRepository;
    private final ShoppingListItemRepository shoppingListItemRepository;
    private final ItemRepository itemRepository;
    private final UnitsRelationRepository unitsRelationRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public ShoppingListServiceImpl(ShoppingListRepository shoppingListRepository,
                                   RecipeRepository recipeRepository,
                                   ItemStorageRepository itemStorageRepository,
                                   ShoppingListItemRepository shoppingListItemRepository,
                                   ItemRepository itemRepository, UnitsRelationRepository unitsRelationRepository,
                                   UserRepository userRepository,
                                   UserService userService) {
        this.recipeRepository = recipeRepository;
        this.itemStorageRepository = itemStorageRepository;
        this.shoppingListItemRepository = shoppingListItemRepository;
        this.shoppingListRepository = shoppingListRepository;
        this.unitsRelationRepository = unitsRelationRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.userService = userService;
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
    public List<ItemStorage> planRecipe(Long recipeId, Authentication authentication) {
        LOGGER.debug("Service: plan Recipe {} based on user {}.", recipeId, authentication.getName());

        if (recipeId == null) {
            throw new ValidationException("Recipe does not exist");
        }
        ApplicationUser user = userService.findApplicationUserByUsername(authentication.getName());
        if (user == null) {
            throw new ValidationException("User does not exist");
        }
        UserGroup group = user.getCurrGroup();
        if (group == null) {
            throw new ValidationException("Storage does not exist");
        }
        Long storageId = group.getStorageId();
        if (storageId == null) {
            throw new ValidationException("Storage does not exist");
        }
        Long shoppingListId = group.getPublicShoppingListId();
        if (shoppingListId == null) {
            throw new ValidationException("Public ShoppingList does not exist");
        }

        Recipe recipe = null;
        List<ItemStorage> storageItems;
        List<ItemStorage> returnList = null;

        try {
            recipe = recipeRepository.findRecipeById(recipeId);
            if (recipe == null) {
                throw new NotFoundException("Could not find recipe with id " + recipeId);
            }
        } catch (ObjectNotFoundException e) {
            throw new NotFoundException("Could not find recipe with id " + recipeId, e);
        }

        try {
            storageItems = itemStorageRepository.findAllByStorageId(storageId);
            if (storageItems == null) {
                throw new NotFoundException("Could not find storage with id " + storageId);
            }
        } catch (ObjectNotFoundException e) {
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
            ItemStorage storedItem = storedItemsMap.get(ingredient.getName());
            if (storedItem == null) {
                // adds items that are not in the storage
                returnSet.add(ingredient);
            } else {
                if (ingredient.getQuantity().equals(storedItem.getQuantity())) {
                    // adds items if there is not enough of them in the storage
                    if (ingredient.getAmount() > storedItem.getAmount()) {
                        returnSet.add(ingredient);
                    }
                } else {
                    UnitsRelation unitsRelation = unitsRelationRepository.findUnitsRelationByBaseUnitAndCalculatedUnit(ingredient.getQuantity().getName(), storedItem.getQuantity().getName());
                    if (unitsRelation != null) {
                        Double relation = unitsRelation.getRelation();
                        if (ingredient.getAmount() * relation > storedItem.getAmount()) {
                            returnSet.add(ingredient);
                        }
                    }
                }
            }
        }
        return returnSet;
    }

    @Override
    public ItemStorage saveItem(ItemStorage itemStorage, Long id) {
        LOGGER.debug("save item in shopping list");
        shoppingListItemRepository.saveAndFlush(itemStorage);
        shoppingListItemRepository.insert(id, itemStorage.getId());

        return itemStorage;

    }

    @Transactional
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
    public List<ItemStorage> workOffShoppingList(String username, List<ItemStorage> boughtItems) {
        LOGGER.debug("Work Off Shoppinglist {}", boughtItems);

        Optional<ApplicationUser> userOptional = userRepository.findUserByUsername(username);
        if (userOptional.isPresent()) {
            ApplicationUser user = userOptional.get();
            UserGroup group = user.getCurrGroup();
            Long storageId = group.getStorageId();

            for (ItemStorage item : boughtItems) {
                ItemStorage itemStorage = itemStorageRepository.getById(item.getId());

                item.setShoppingListId(null);
                item.setStorageId(1L);

                shoppingListItemRepository.saveAndFlush(item);
            }
        }

        return boughtItems;
    }

}
