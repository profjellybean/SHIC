package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnchangeableException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemStorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.RecipeService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import javax.transaction.Transactional;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Objects;

@Service
public class RecipeServiceImpl implements RecipeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final RecipeRepository recipeRepository;
    private final ItemStorageRepository itemStorageRepository;
    private final UserService userService;

    public RecipeServiceImpl(RecipeRepository recipeRepository, ItemStorageRepository itemStorageRepository, UserService userService) {
        this.recipeRepository = recipeRepository;
        this.itemStorageRepository = itemStorageRepository;
        this.userService = userService;
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
        if (recipe.getGroupId() == null) {
            throw new UnchangeableException("This recipe cannot be updated because it is available for all groups");
        }
        Recipe excisting = findRecipeById(recipe.getId());
        if (excisting != null) {
            return recipeRepository.saveAndFlush(recipe);
        } else {
            throw new NotFoundException("No Recipe expists in Database with Id: " + recipe.getId());
        }
    }


    @Override
    @Transactional
    public void deleteRecipe(String userName, Long id) {
        LOGGER.debug("Service: delete recipe by id: {}", id);
        Long registerId = userService.loadGroupRegisterIdByUsername(userName);
        if (findRecipeById(id) == null || !Objects.equals(registerId, findRecipeById(id).getGroupId())) {
            throw new NotFoundException("you are not authorized");
        }
        Recipe helpRecipe = findRecipeById(id);
        if (helpRecipe == null) {
            throw new NotFoundException("recipe not found");
        }
        recipeRepository.delete(helpRecipe);
    }
}
