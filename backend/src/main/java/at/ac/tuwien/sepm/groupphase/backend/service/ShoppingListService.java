package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;

import java.util.List;

public interface ShoppingListService {

    /**
     * plans a recipe. Checks which of the required ingredients are not present
     * and sets them on the shopping list
     *
     * @param recipeId id of recipe that user wants to cook
     * @param storageId id of storage of the group, of which the user is part of
     * @return a List of all ingredients that were added to the ShoppingList
     */
    List<ItemStorage> planRecipe(Long recipeId, Long storageId);

}
