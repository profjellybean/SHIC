package at.ac.tuwien.sepm.groupphase.backend.util;

import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemStorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Component
public class ShoppingListValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RecipeRepository recipeRepository;
    private final UserService userService;
    private final ItemStorageRepository itemStorageRepository;

    public ShoppingListValidator(RecipeRepository recipeRepository, UserService userService,
                                 ItemStorageRepository itemStorageRepository) {
        this.recipeRepository = recipeRepository;
        this.userService = userService;
        this.itemStorageRepository = itemStorageRepository;
    }

    public void validate_planRecipe(Long recipeId, String userName, Integer numberOfPeople) {
        LOGGER.debug("Service: validating planRecipe recipeId={},userName={},numberOfPeople={}", recipeId, numberOfPeople, userName);

        validate_putRecipeOnShoppingList(recipeId, userName, numberOfPeople);

        Long storageId = userService.loadGroupStorageByUsername(userName);
        if (storageId == null) {
            throw new NotFoundException("Storage could not be found");
        }
        List<ItemStorage> storageItems = itemStorageRepository.findAllByStorageId(storageId);
        if (storageItems == null) {
            throw new NotFoundException("Could not find storage with id " + storageId);
        }
    }

    public void validate_putRecipeOnShoppingList(Long recipeId, String userName, Integer numberOfPeople) {
        LOGGER.debug("Service: validating putRecipeOnShoppingList recipeId={},userName={},numberOfPeople={}", recipeId, numberOfPeople, userName);

        if (recipeId == null) {
            throw new ValidationException("Recipe is not specified");
        }
        Recipe recipe = recipeRepository.findRecipeById(recipeId);
        if (recipe == null) {
            throw new NotFoundException("Could not find recipe with id " + recipeId);
        }
        if (userName == null) {
            throw new ValidationException("User is not specified");
        }
        Long shoppingListId = userService.getPublicShoppingListIdByUsername(userName);
        if (shoppingListId == null) {
            throw new NotFoundException("Public ShoppingList could not be found");
        }
        if (numberOfPeople == null || numberOfPeople < 1) {
            throw new ValidationException("Number of people has to be 1 or bigger");
        }
        if (numberOfPeople > 100) {
            throw new ValidationException("Number of people can not be bigger than 100");
        }
    }


}
