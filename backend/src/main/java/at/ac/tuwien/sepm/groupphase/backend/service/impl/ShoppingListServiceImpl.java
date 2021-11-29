package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.StorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ShoppingListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ShoppingListServiceImpl implements ShoppingListService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ShoppingListRepository shoppingListRepository;
    private final RecipeRepository recipeRepository;
    private final StorageRepository storageRepository;

    public ShoppingListServiceImpl(ShoppingListRepository shoppingListRepository,
                                   RecipeRepository recipeRepository,
                                   StorageRepository storageRepository) {
        this.shoppingListRepository = shoppingListRepository;
        this.recipeRepository = recipeRepository;
        this.storageRepository = storageRepository;
    }

    @Override
    public ShoppingList planRecipe(Long recipeId, Long storageId) {
        LOGGER.debug("Service: plan Recipe {} based on storage {}.", recipeId, storageId);

        // TODO: check if user has access
        // TODO: catch errors that might occur accessing the repository
        Recipe recipe = recipeRepository.getById(recipeId);
        ItemStorage storage = storageRepository.getById(storageId);

        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setName("Ingredients required for recipe: "+recipe.getName());
        // TODO compare item sets
        // shoppingList.setItems(compareItemSets(recipe.getIngredients(), storage.getItems()));

        return shoppingListRepository.save(shoppingList);
    }

    /**
     * Compares two sets of items.
     * Checks which items of recipeIngredients don't appear in storedItems.
     * If they appear, the amount of the Items is compared.
     *
     * @param recipeIngredients set of items e.g. representing ingredients of a recipe
     * @param storedItems set of items e.g. representing the stored Items in a Storage
     * @return Set of all Items that occur in recipeIngredients but not in storedItems
     *      OR occur in both, but the amount in recipeIngredients is bigger than the amount in storedItems
     */
    private Set<Item> compareItemSets(Set<Item> recipeIngredients, Set<Item> storedItems) {
        LOGGER.debug("Service: compareItemLists");
        Set<Item> returnSet = new HashSet<>();
        // stores items from Set into Map, because one cannot get items from a Set
        Map<Long, Item> storedItemsMap = storedItems.stream().collect(Collectors.toMap(Item::getId, Function.identity()));
        for (Item ingredient:
            recipeIngredients) {
            if(!storedItems.contains(ingredient)) {
                // adds items that are not in the storage
                returnSet.add(ingredient);
            } else {
                Item storedItem = storedItemsMap.get(ingredient.getId());
                if(ingredient.getQuantity().equals(storedItem.getQuantity())) {
                    // adds items if there is not enough in the storage
                    if(ingredient.getAmount() > storedItem.getAmount()) returnSet.add(ingredient);
                } else {
                    // TODO recalculate unitOfQuantity
                }
            }
        }
        return returnSet;
    }
}
