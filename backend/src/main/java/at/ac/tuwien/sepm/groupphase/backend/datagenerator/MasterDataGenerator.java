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
    private final MessageDataGenerator messageDataGenerator;
    private final RecipeDataGenerator recipeDataGenerator;
    private final ShoppingListDataGenerator shoppingListDataGenerator;
    private final StorageDataGenerator storageDataGenerator;
    private final UserDataGenerator userDataGenerator;


    public MasterDataGenerator(MessageDataGenerator messageDataGenerator, RecipeDataGenerator recipeDataGenerator, ShoppingListDataGenerator shoppingListDataGenerator, StorageDataGenerator storageDataGenerator, UserDataGenerator userDataGenerator) {
        this.messageDataGenerator = messageDataGenerator;
        this.recipeDataGenerator = recipeDataGenerator;
        this.shoppingListDataGenerator = shoppingListDataGenerator;
        this.storageDataGenerator = storageDataGenerator;
        this.userDataGenerator = userDataGenerator;
    }

    @PostConstruct
    private void generateMessage() {
        messageDataGenerator.generateMessage();
        storageDataGenerator.generateShoppingList();
        recipeDataGenerator.generateRecipes();
        shoppingListDataGenerator.generateShoppingList();
        userDataGenerator.generateShoppingList();
    }
}
