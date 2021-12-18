package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.Location;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemStorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UnitOfQuantityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

//@Profile("generateData")
@Component
public class ShoppingListDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_SHOPPINGLISTS_TO_GENERATE = 5;
    private static final String TEST_SHOPPINGLIST_NAME = "Name";
    private static final String TEST_SHOPPINGLIST_NOTES = "Notes of the shoppinglist";
    private static final ApplicationUser TEST_SHOPPINGLIST_OWNER = null;
    private static final Long ID_OF_SHOPPINGLIST = 1L;

    private Set<ItemStorage> testShoppingListItems = null;

    private final ShoppingListRepository shoppingListRepository;
    private final ItemStorageRepository itemStorageRepository;
    private final UnitOfQuantityRepository unitOfQuantityRepository;

    private final UnitOfQuantityDataGenerator unitOfQuantityDataGenerator;

    public ShoppingListDataGenerator(ShoppingListRepository shoppingListRepository, ItemStorageRepository itemStorageRepository, UnitOfQuantityRepository unitOfQuantityRepository, UnitOfQuantityDataGenerator unitOfQuantityDataGenerator) {
        this.shoppingListRepository = shoppingListRepository;
        this.itemStorageRepository = itemStorageRepository;
        this.unitOfQuantityRepository = unitOfQuantityRepository;
        this.unitOfQuantityDataGenerator = unitOfQuantityDataGenerator;
    }

    @PostConstruct
    void generateShoppingList() {

        this.unitOfQuantityDataGenerator.generateUnitOfQuantity();

        List<UnitOfQuantity> unitList = unitOfQuantityRepository.findAll();
        Map<String, Long> mappedUnits = new HashMap<>();
        for (UnitOfQuantity unit :
            unitList) {
            mappedUnits.put(unit.getName(), unit.getId());
        }


        if (shoppingListRepository.findAll().size() > 0) {
            LOGGER.debug("shoppinglist already generated");
        } else {
            LOGGER.debug("generating {} shoppinglist entries", NUMBER_OF_SHOPPINGLISTS_TO_GENERATE);
            for (int i = 0; i < NUMBER_OF_SHOPPINGLISTS_TO_GENERATE; i++) {
                ShoppingList shoppingList = ShoppingList.ShoppingListBuilder.aShoppingList()
                    .withName(TEST_SHOPPINGLIST_NAME + " " + i)
                    .withNotes(TEST_SHOPPINGLIST_NOTES + " " + i)
                    .withOwner(TEST_SHOPPINGLIST_OWNER)
                    .withItems(testShoppingListItems)
                    .build();
                LOGGER.debug("saving shoppinglist {}", shoppingList);
                shoppingListRepository.saveAndFlush(shoppingList);
            }


            //ItemStorage
            ItemStorage mushrooms = new ItemStorage("Mushrooms", null, null, null, 200,
                Location.fridge, mappedUnits.get("g"), null, ID_OF_SHOPPINGLIST);
            itemStorageRepository.save(mushrooms);
            ItemStorage pasta = new ItemStorage("Pasta", null, null, null, 500,
                Location.shelf, mappedUnits.get("g"), null, ID_OF_SHOPPINGLIST);
            itemStorageRepository.save(pasta);
            ItemStorage whippedCream = new ItemStorage("Whipped Cream", null, null, null,
                1, Location.fridge, mappedUnits.get("L"), null, ID_OF_SHOPPINGLIST);
            itemStorageRepository.save(whippedCream);
            ItemStorage parsley = new ItemStorage("Parsley", "Genovese", null, null,
                200, Location.fridge, mappedUnits.get("g"), null, ID_OF_SHOPPINGLIST);
            itemStorageRepository.save(parsley);

            testShoppingListItems = new HashSet<ItemStorage>();
            testShoppingListItems.add(mushrooms);
            testShoppingListItems.add(pasta);
            testShoppingListItems.add(whippedCream);
            testShoppingListItems.add(parsley);

            ShoppingList shoppingList = shoppingListRepository.getById(1L);
            shoppingList.setItems(testShoppingListItems);
            shoppingListRepository.saveAndFlush(shoppingList);
        }
    }
}
