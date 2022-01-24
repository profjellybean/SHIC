package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.Location;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemStorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListItemRepository;
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
    private static final Long ID_OF_SHOPPINGLIST = 2L;
    private static final boolean GENERATE_REAL_SHOPPINGLIST = true;

    private Set<ItemStorage> testShoppingListItems = null;

    private final ShoppingListRepository shoppingListRepository;
    private final ItemStorageRepository itemStorageRepository;
    private final UnitOfQuantityRepository unitOfQuantityRepository;
    private final ShoppingListItemRepository shoppingListItemRepository;

    private final UnitOfQuantityDataGenerator unitOfQuantityDataGenerator;

    public ShoppingListDataGenerator(ShoppingListRepository shoppingListRepository, ItemStorageRepository itemStorageRepository,
                                     UnitOfQuantityRepository unitOfQuantityRepository, UnitOfQuantityDataGenerator unitOfQuantityDataGenerator,
                                     ShoppingListItemRepository shoppingListItemRepository) {
        this.shoppingListRepository = shoppingListRepository;
        this.itemStorageRepository = itemStorageRepository;
        this.unitOfQuantityRepository = unitOfQuantityRepository;
        this.unitOfQuantityDataGenerator = unitOfQuantityDataGenerator;
        this.shoppingListItemRepository = shoppingListItemRepository;
    }

    //@PostConstruct
    void generateShoppingList() {

        this.unitOfQuantityDataGenerator.generateUnitOfQuantity();

        List<UnitOfQuantity> unitList = unitOfQuantityRepository.findAll();
        Map<String, UnitOfQuantity> mappedUnits = new HashMap<>();
        for (UnitOfQuantity unit :
            unitList) {
            mappedUnits.put(unit.getName(), unit);
        }


        if (!GENERATE_REAL_SHOPPINGLIST) {
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
        } else if (shoppingListItemRepository.findAll().isEmpty()) {
            LOGGER.debug("generating items for shoppinglist");

            //ItemStorage
            ItemStorage mushrooms = new ItemStorage("Mushrooms", null, null, null, 200,
                Location.fridge.toString(), mappedUnits.get("g"), null, ID_OF_SHOPPINGLIST);
            mushrooms = itemStorageRepository.saveAndFlush(mushrooms);
            ItemStorage pasta = new ItemStorage("Pasta", null, null, null, 500,
                Location.shelf.toString(), mappedUnits.get("g"), null, ID_OF_SHOPPINGLIST);
            pasta = itemStorageRepository.saveAndFlush(pasta);
            ItemStorage whippedCream = new ItemStorage("Whipped Cream", null, null, null,
                1, Location.fridge.toString(), mappedUnits.get("L"), null, ID_OF_SHOPPINGLIST);
            whippedCream = itemStorageRepository.saveAndFlush(whippedCream);
            ItemStorage parsley = new ItemStorage("Parsley", "Genovese", null, null,
                200, Location.fridge.toString(), mappedUnits.get("g"), null, ID_OF_SHOPPINGLIST);
            parsley = itemStorageRepository.saveAndFlush(parsley);

            ItemStorage fish = new ItemStorage("Fish", null, null, null,
                200, Location.fridge.toString(), mappedUnits.get("g"), null, ID_OF_SHOPPINGLIST);
            fish = itemStorageRepository.saveAndFlush(fish);
            ItemStorage chips = new ItemStorage("Chips", "Spicy", null, null,
                100, Location.fridge.toString(), mappedUnits.get("g"), null, ID_OF_SHOPPINGLIST);
            chips = itemStorageRepository.saveAndFlush(chips);
            ItemStorage salad = new ItemStorage("Salad", null, null, null,
                1, Location.fridge.toString(), mappedUnits.get("pieces"), null, ID_OF_SHOPPINGLIST);
            salad = itemStorageRepository.saveAndFlush(salad);
            ItemStorage fries = new ItemStorage("Fries", null, null, null,
                500, Location.fridge.toString(), mappedUnits.get("g"), null, ID_OF_SHOPPINGLIST);
            fries = itemStorageRepository.saveAndFlush(fries);
            ItemStorage cake = new ItemStorage("Cake", "For Emmis Birthday", null, null,
                1, Location.fridge.toString(), mappedUnits.get("pieces"), null, ID_OF_SHOPPINGLIST);
            cake = itemStorageRepository.saveAndFlush(cake);
            ItemStorage bananas = new ItemStorage("Bananas", null, null, null,
                1, Location.fridge.toString(), mappedUnits.get("kg"), null, ID_OF_SHOPPINGLIST);
            bananas = itemStorageRepository.saveAndFlush(bananas);
            ItemStorage ananas = new ItemStorage("Ananas", null, null, null,
                1, Location.fridge.toString(), mappedUnits.get("pieces"), null, ID_OF_SHOPPINGLIST);
            ananas = itemStorageRepository.saveAndFlush(ananas);
            ItemStorage garlic = new ItemStorage("Garlic", null, null, null,
                3, Location.fridge.toString(), mappedUnits.get("pieces"), null, ID_OF_SHOPPINGLIST);
            garlic = itemStorageRepository.saveAndFlush(garlic);
            ItemStorage onions = new ItemStorage("Onions", null, null, null,
                100, Location.fridge.toString(), mappedUnits.get("g"), null, ID_OF_SHOPPINGLIST);
            onions = itemStorageRepository.saveAndFlush(onions);
            ItemStorage orange = new ItemStorage("Oranges", null, null, null,
                5, Location.fridge.toString(), mappedUnits.get("pieces"), null, ID_OF_SHOPPINGLIST);
            orange = itemStorageRepository.saveAndFlush(orange);
            ItemStorage lemon = new ItemStorage("Lemons", null, null, null,
                3, Location.fridge.toString(), mappedUnits.get("pieces"), null, ID_OF_SHOPPINGLIST);
            lemon = itemStorageRepository.saveAndFlush(lemon);
            ItemStorage milk = new ItemStorage("Milk", null, null, null,
                4, Location.fridge.toString(), mappedUnits.get("L"), null, ID_OF_SHOPPINGLIST);
            milk = itemStorageRepository.saveAndFlush(milk);
            ItemStorage oliveOil = new ItemStorage("Olive Oil", null, null, null,
                1, Location.fridge.toString(), mappedUnits.get("L"), null, ID_OF_SHOPPINGLIST);
            oliveOil = itemStorageRepository.saveAndFlush(oliveOil);
            ItemStorage lemonJuice = new ItemStorage("Lemon Juice", null, null, null,
                250, Location.fridge.toString(), mappedUnits.get("ml"), null, ID_OF_SHOPPINGLIST);
            lemonJuice = itemStorageRepository.saveAndFlush(lemonJuice);
            ItemStorage chocolate = new ItemStorage("Chocolate", null, null, null,
                200, Location.fridge.toString(), mappedUnits.get("g"), null, ID_OF_SHOPPINGLIST);
            chocolate = itemStorageRepository.saveAndFlush(chocolate);
            ItemStorage waterMelon = new ItemStorage("Water Melon", null, null, null,
                1, Location.fridge.toString(), mappedUnits.get("pieces"), null, ID_OF_SHOPPINGLIST);
            waterMelon = itemStorageRepository.saveAndFlush(waterMelon);
            ItemStorage greenTea = new ItemStorage("Green Tea", null, null, null,
                2, Location.fridge.toString(), mappedUnits.get("L"), null, ID_OF_SHOPPINGLIST);
            greenTea = itemStorageRepository.saveAndFlush(greenTea);
            ItemStorage baguette = new ItemStorage("Baguette", null, null, null,
                2, Location.fridge.toString(), mappedUnits.get("pieces"), null, ID_OF_SHOPPINGLIST);
            baguette = itemStorageRepository.saveAndFlush(baguette);

            testShoppingListItems = new HashSet<ItemStorage>();
            testShoppingListItems.add(mushrooms);
            testShoppingListItems.add(pasta);
            testShoppingListItems.add(whippedCream);
            testShoppingListItems.add(parsley);

            testShoppingListItems.add(fish);
            testShoppingListItems.add(chips);
            testShoppingListItems.add(salad);
            testShoppingListItems.add(fries);
            testShoppingListItems.add(cake);
            testShoppingListItems.add(bananas);
            testShoppingListItems.add(ananas);
            testShoppingListItems.add(garlic);
            testShoppingListItems.add(onions);
            testShoppingListItems.add(orange);
            testShoppingListItems.add(lemon);
            testShoppingListItems.add(milk);
            testShoppingListItems.add(oliveOil);
            testShoppingListItems.add(lemonJuice);
            testShoppingListItems.add(chocolate);
            testShoppingListItems.add(waterMelon);
            testShoppingListItems.add(greenTea);
            testShoppingListItems.add(baguette);

            for (ItemStorage item :
                testShoppingListItems) {
                shoppingListItemRepository.insert(ID_OF_SHOPPINGLIST, item.getId());
            }
            /*
            ShoppingList shoppingList = shoppingListRepository.getById(ID_OF_SHOPPINGLIST);
            shoppingList.setItems(testShoppingListItems);
            shoppingListRepository.saveAndFlush(shoppingList);
             */
        }
    }
}
