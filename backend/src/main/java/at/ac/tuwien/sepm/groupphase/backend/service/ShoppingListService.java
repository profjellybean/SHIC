package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;

public interface ShoppingListService {

    /**
     * plans a recipe. Checks which of the required ingredients are not present
     * and sets them on the shopping list
     *
     * @param recipeId id of recipe that user wants to cook
     * @param storageId id of storage of the group, of which the user is part of
     * @return a new ShoppingList with required, non-existing ingredients
     */
    ShoppingList planRecipe(Long recipeId, Long storageId);

}
