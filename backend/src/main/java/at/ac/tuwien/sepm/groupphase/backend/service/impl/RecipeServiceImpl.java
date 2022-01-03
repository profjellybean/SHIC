package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepm.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.RecipeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class RecipeServiceImpl implements RecipeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public List<Recipe> findAll() {
        LOGGER.debug("Find all recipes");
        return recipeRepository.findAll();
    }

    @Override
    public Recipe findRecipeById(Long id) {
        LOGGER.debug("Find one recipe by id");
        return recipeRepository.findRecipeById(id);
    }

    @Override
    public Recipe addRecipe(Recipe recipe) {
        LOGGER.debug("Add one recipe");
        return recipeRepository.saveAndFlush(recipe);
    }
}
