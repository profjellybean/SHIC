package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;

import java.util.List;

public interface RecipeService {

    /**
     * Find all recipe entries.
     *
     * @return ordered list of all recipe entries
     */
    List<Recipe> findAll();

    /**
     * Find one recipe entry by id.
     *
     * @return recipe with given id
     */
    Recipe findRecipeById(Long id);

    /**
     * Adds a new recipe.
     *
     * @param recipe to add
     * @return the added recipe
     */
    Recipe addRecipe(Recipe recipe);

    /**
     * Saves updated existing recipe (specified in the recipe itself).
     *
     * @param recipe the updated version of an existing recipe
     * @param groupId to check if this is recipe is available for all groups then it cannot be updated
     *
     * @return the updated recipe
     */
    Recipe updateRecipe(Recipe recipe, Long groupId);

    /**
     * delete recipe by id.
     *
     * @param id of the recipe
     *
     */
    void deleteRecipe(String userName, Long id);

    /**
     * find recipe by substring.
     *
     * @param name that has to occur in name of the recipe
     *
     * @return list of all recipes with names that contain specified String
     */
    List<Recipe> findRecipeBySubstring(String name);
}
