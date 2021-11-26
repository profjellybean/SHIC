package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;

public interface ShoppingListService {

    ShoppingList planRecipe(Long recipeId, Long storageId);

}
