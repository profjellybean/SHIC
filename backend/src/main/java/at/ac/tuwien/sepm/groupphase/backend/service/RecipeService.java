package at.ac.tuwien.sepm.groupphase.backend.service;

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

}
