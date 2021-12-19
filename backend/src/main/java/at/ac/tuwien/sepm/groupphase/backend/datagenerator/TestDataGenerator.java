package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.repository.UnitOfQuantityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

/**
 * class used to generate Data for specific test cases.
 */
@Component
public class TestDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final RecipeDataGenerator recipeDataGenerator;
    private final ShoppingListDataGenerator shoppingListDataGenerator;
    private final StorageDataGenerator storageDataGenerator;
    private final UserDataGenerator userDataGenerator;
    private final ItemStorageDataGenerator itemStorageDataGenerator;
    private final ItemDataGenerator itemDataGenerator;
    private final UnitOfQuantityDataGenerator unitOfQuantityDataGenerator;

    public TestDataGenerator(RecipeDataGenerator recipeDataGenerator,
                               ShoppingListDataGenerator shoppingListDataGenerator,
                               StorageDataGenerator storageDataGenerator,
                               UserDataGenerator userDataGenerator,
                               ItemStorageDataGenerator itemStorageDataGenerator,
                               UnitOfQuantityDataGenerator unitOfQuantityDataGenerator,
                               UnitOfQuantityRepository unitOfQuantityRepository,
                               ItemDataGenerator itemDataGenerator) {
        this.recipeDataGenerator = recipeDataGenerator;
        this.shoppingListDataGenerator = shoppingListDataGenerator;
        this.storageDataGenerator = storageDataGenerator;
        this.userDataGenerator = userDataGenerator;
        this.itemStorageDataGenerator = itemStorageDataGenerator;
        this.itemDataGenerator = itemDataGenerator;
        this.unitOfQuantityDataGenerator = unitOfQuantityDataGenerator;
    }

    /**
     * generates Data used in Tests for the planRecipe(Long recipeId, Authentication auth) method.
     */
    public void generateData_planRecipe() {
        LOGGER.debug("Generating Data for planning Recipe");
        recipeDataGenerator.generateRecipes(); // includes UnitOfQuantity
        userDataGenerator.generateUser(); // includes ShoppingList and Storage
        itemStorageDataGenerator.generateItemStorage(); // includes UnitOfQuantity and Storage
    }
}
