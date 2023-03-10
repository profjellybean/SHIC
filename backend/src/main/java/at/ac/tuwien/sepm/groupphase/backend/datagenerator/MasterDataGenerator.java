package at.ac.tuwien.sepm.groupphase.backend.datagenerator;


import at.ac.tuwien.sepm.groupphase.backend.repository.UnitOfQuantityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
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
    private final BillDataGenerator billDataGenerator;

    public MasterDataGenerator(RecipeDataGenerator recipeDataGenerator,
                               ShoppingListDataGenerator shoppingListDataGenerator,
                               StorageDataGenerator storageDataGenerator,
                               UserDataGenerator userDataGenerator,
                               ItemStorageDataGenerator itemStorageDataGenerator,
                               UnitOfQuantityDataGenerator unitOfQuantityDataGenerator,
                               UnitOfQuantityRepository unitOfQuantityRepository,
                               ItemDataGenerator itemDataGenerator,
                               BillDataGenerator billDataGenerator) {
        this.recipeDataGenerator = recipeDataGenerator;
        this.shoppingListDataGenerator = shoppingListDataGenerator;
        this.storageDataGenerator = storageDataGenerator;
        this.userDataGenerator = userDataGenerator;
        this.itemStorageDataGenerator = itemStorageDataGenerator;
        this.itemDataGenerator = itemDataGenerator;
        this.unitOfQuantityDataGenerator = unitOfQuantityDataGenerator;
        this.billDataGenerator = billDataGenerator;
    }

    @PostConstruct
    public void generateData() throws IOException {
        LOGGER.debug("Generating Data");
        storageDataGenerator.generateStorage();
        unitOfQuantityDataGenerator.generateUnitOfQuantity();
        unitOfQuantityDataGenerator.generateUnitsRelations();
        userDataGenerator.generateUser();
        //userDataGenerator.generateApplicationUser();
        itemDataGenerator.generateItem();
        itemStorageDataGenerator.generateItemStorage();
        recipeDataGenerator.generateRecipes();
        //shoppingListDataGenerator.generateShoppingList();
        billDataGenerator.generateBills();
    }

    public void generateData_planRecipe() throws IOException {
        LOGGER.debug("Generating Data for planning Recipe");
        recipeDataGenerator.generateRecipes(); // includes UnitOfQuantity
        userDataGenerator.generateUser(); // includes ShoppingList and Storage
        itemStorageDataGenerator.generateItemStorage(); // includes UnitOfQuantity and Storage
    }


}
