package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.StorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ShoppingListService;
import org.hibernate.LazyInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ShoppingListServiceImpl implements ShoppingListService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    //private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final StorageRepository storageRepository;

    public ShoppingListServiceImpl(//UserRepository userRepository,
                                   RecipeRepository recipeRepository,
                                   StorageRepository storageRepository) {
        //this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.storageRepository = storageRepository;
    }

    @Override
    @Transactional
    public List<ItemStorage> planRecipe(Long recipeId, Long storageId) {
        LOGGER.debug("Service: plan Recipe {} based on storage {}.", recipeId, storageId);
        Recipe recipe = null;
        List<ItemStorage> storageItems;

        List<ItemStorage> returnList = null;
        // TODO: check if user has access

        // TODO: catch errors that might occur accessing the repository
        try {
            recipe = recipeRepository.findRecipeById(recipeId);
            System.out.println("RECIPE: "+recipe); // TODO delete line
        } catch (EntityNotFoundException e) { // TODO catch other error
            throw new NotFoundException("Could not find recipe with id "+recipeId, e);
        }

        try {
            storageItems = storageRepository.findAllByStorageId(storageId);
            System.out.println("STORAGE: "+storageItems); // TODO delete line
        } catch (EntityNotFoundException e) { // TODO catch other error
            throw new NotFoundException("Could not find storage with id "+storageId, e);
        }

        /*
        recipe.setIngredients(new HashSet<ItemStorage>(Arrays.asList(
            new ItemStorage(1L, "Name1", "notes of item 1", null, null, 10, null, UnitOfQuantity.kg, 1L),
            new ItemStorage(2L, "Name2", "notes of item 2", null, null, 20, null, UnitOfQuantity.kg, 1L))));

        storageItems = Arrays.asList(
            new ItemStorage(2L, "Name2", "notes of item 2", null, null, 20, null, UnitOfQuantity.kg, 1L),
            new ItemStorage(3L, "Name3", "notes of item 3", null, null, 30, null, UnitOfQuantity.kg, 1L));
         */

        // TODO compare item sets
        returnList = compareItemSets(recipe.getIngredients(), storageItems);

        // TODO add items to existing list
        return returnList;
        //return shoppingListRepository.save(shoppingList);
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
    private List<ItemStorage> compareItemSets(Set<ItemStorage> recipeIngredients, List<ItemStorage> storedItems) {
        LOGGER.debug("Service: compareItemLists");
        List<ItemStorage> returnSet = new LinkedList<>();
        // stores items from Set into Map, because one cannot get items from a Set
        Map<String, ItemStorage> storedItemsMap = storedItems.stream().collect(Collectors.toMap(ItemStorage::getName, Function.identity()));
        for (ItemStorage ingredient:
            recipeIngredients) {
            if(!storedItems.contains(ingredient)) {
                // adds items that are not in the storage
                returnSet.add(ingredient);
            } else {
                ItemStorage storedItem = storedItemsMap.get(ingredient.getName());
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
