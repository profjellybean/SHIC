package at.ac.tuwien.sepm.groupphase.backend.datagenerator;


import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.repository.UnitOfQuantityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;

@Profile("generateData")
@Component
public class MasterDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final RecipeDataGenerator recipeDataGenerator;
    private final ShoppingListDataGenerator shoppingListDataGenerator;
    private final StorageDataGenerator storageDataGenerator;
    private final UserDataGenerator userDataGenerator;
    private final ItemStorageDataGenerator itemStorageDataGenerator;
    private final ItemDataGenerator itemDataGenerator;
    private final UnitOfQuantityDataGenerator unitOfQuantityDataGenerator;

    public MasterDataGenerator(RecipeDataGenerator recipeDataGenerator,
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

    @PostConstruct
    private void generateData() {
        LOGGER.debug("Generating Data");
        unitOfQuantityDataGenerator.generateUnitOfQuantity();
        userDataGenerator.generateUser();
        itemDataGenerator.generateItem();
        itemStorageDataGenerator.generateItemStorage();
        storageDataGenerator.generateStorage();
        recipeDataGenerator.generateRecipes();
        shoppingListDataGenerator.generateShoppingList();

    }

}
