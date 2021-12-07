package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;

//@Profile("generateData")
@Component
public class ShoppingListDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_SHOPPINGLISTS_TO_GENERATE = 5;
    private static final String TEST_SHOPPINGLIST_NAME = "Name";
    private static final String TEST_SHOPPINGLIST_NOTES = "Notes of the shoppinglist";
    private static final ApplicationUser TEST_SHOPPINGLIST_OWNER = null;
    private static final Set<ItemStorage> TEST_SHOPPINGLIST_ITEMS = null;
    //private static final Set<Item> TEST_SHOPPINGLIST_ITEMS = new HashSet<Item>(Arrays.asList(new Item(1L), new Item(2L)));

    private final ShoppingListRepository shoppingListRepository;

    public ShoppingListDataGenerator(ShoppingListRepository shoppingListRepository) {
        this.shoppingListRepository = shoppingListRepository;
    }

    @PostConstruct
    void generateShoppingList() {
        if(shoppingListRepository.findAll().size() > 0) {
            LOGGER.debug("shoppinglist already generated");
        } else {
            LOGGER.debug("generating {} shoppinglist entries", NUMBER_OF_SHOPPINGLISTS_TO_GENERATE);
            for (int i = 0; i < NUMBER_OF_SHOPPINGLISTS_TO_GENERATE; i++) {
                ShoppingList shoppingList = ShoppingList.ShoppingListBuilder.aShoppingList()
                    .withName(TEST_SHOPPINGLIST_NAME+" "+i)
                    .withNotes(TEST_SHOPPINGLIST_NOTES+" "+i)
                    .withOwner(TEST_SHOPPINGLIST_OWNER)
                    .withItems(TEST_SHOPPINGLIST_ITEMS)
                    .build();
                LOGGER.debug("saving shoppinglist {}", shoppingList);
                shoppingListRepository.save(shoppingList);
            }
        }
    }
}
