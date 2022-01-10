package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.Location;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnchangeableException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemStorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.RecipeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.List;

@Service
public class RecipeServiceImpl implements RecipeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final RecipeRepository recipeRepository;
    private final ItemStorageRepository itemStorageRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository, ItemStorageRepository itemStorageRepository) {
        this.recipeRepository = recipeRepository;
        this.itemStorageRepository = itemStorageRepository;
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
        for (ItemStorage item : recipe.getIngredients()) {
            itemStorageRepository.saveAndFlush(item);
        }
        return recipeRepository.saveAndFlush(recipe);
    }

    @Override
    public Recipe updateRecipe(Recipe recipe, Long groupId) {
        LOGGER.debug("Service: Update recipe {}", recipe);
        if (groupId == null) {
            throw new UnchangeableException("This recipe cannot be updated because it is available for all groups");
        }
        return recipeRepository.saveAndFlush(recipe);
    }
}
