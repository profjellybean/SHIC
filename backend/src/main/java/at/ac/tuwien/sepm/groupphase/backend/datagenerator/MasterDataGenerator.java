package at.ac.tuwien.sepm.groupphase.backend.datagenerator;


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


    public MasterDataGenerator(RecipeDataGenerator recipeDataGenerator,
                               ShoppingListDataGenerator shoppingListDataGenerator,
                               StorageDataGenerator storageDataGenerator,
                               UserDataGenerator userDataGenerator,
                               ItemStorageDataGenerator itemStorageDataGenerator,
                               ItemDataGenerator itemDataGenerator) {
        this.recipeDataGenerator = recipeDataGenerator;
        this.shoppingListDataGenerator = shoppingListDataGenerator;
        this.storageDataGenerator = storageDataGenerator;
        this.userDataGenerator = userDataGenerator;
        this.itemStorageDataGenerator = itemStorageDataGenerator;
        this.itemDataGenerator = itemDataGenerator;
    }

    @PostConstruct
    private void generateData() {
        userDataGenerator.generateUser();
        itemDataGenerator.generateItem();
        itemStorageDataGenerator.generateItemStorage();
        storageDataGenerator.generateStorage();
        recipeDataGenerator.generateRecipes();
        shoppingListDataGenerator.generateShoppingList();

    }
}
