package at.ac.tuwien.sepm.groupphase.backend.datagenerator;


import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.LocationClass;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.Location;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemStorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UnitOfQuantityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Profile("generateData")
@Component
public class ItemStorageDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_ITEMSTORAGES = 5;
    private static final Long ID_OF_STORAGE = 6L;
    private static final Long ID_OF_SECOND_STORAGE = 1L;
    private static final Long ID_OF_SHOPPINGLIST = 2L;
    private static final boolean CREATE_REAL_ITEMSTORAGES = true;
    private final ItemStorageRepository itemStorageRepository;
    private final UnitOfQuantityRepository unitOfQuantityRepository;
    private final StorageDataGenerator storageDataGenerator;
    private final ShoppingListDataGenerator shoppingListDataGenerator;
    private final UserDataGenerator userDataGenerator;
    private final LocationRepository locationRepository;

    private final UnitOfQuantityDataGenerator unitOfQuantityDataGenerator;


    public ItemStorageDataGenerator(ItemStorageRepository itemStorageRepository,
                                    UnitOfQuantityRepository unitOfQuantityRepository,
                                    UnitOfQuantityDataGenerator unitOfQuantityDataGenerator,
                                    StorageDataGenerator storageDataGenerator,
                                    ShoppingListDataGenerator shoppingListDataGenerator, UserDataGenerator userDataGenerator, LocationRepository locationRepository) {
        this.itemStorageRepository = itemStorageRepository;
        this.unitOfQuantityRepository = unitOfQuantityRepository;
        this.unitOfQuantityDataGenerator = unitOfQuantityDataGenerator;

        this.storageDataGenerator = storageDataGenerator;
        this.shoppingListDataGenerator = shoppingListDataGenerator;
        this.userDataGenerator = userDataGenerator;
        this.locationRepository = locationRepository;
    }

    //@PostConstruct
    void generateItemStorage() throws IOException {
        if (itemStorageRepository.findAll().size() > 15) {
            LOGGER.debug("ItemStorage already generated");
        } else if (CREATE_REAL_ITEMSTORAGES) {
            LOGGER.debug("generating ItemStorage");

            this.storageDataGenerator.generateStorage();
            this.unitOfQuantityDataGenerator.generateUnitOfQuantity();
            this.userDataGenerator.generateUser();
            this.shoppingListDataGenerator.generateShoppingList();

            locationRepository.saveAndFlush(new LocationClass(Location.fridge.toString(), null));
            locationRepository.saveAndFlush(new LocationClass(Location.freezer.toString(), null));
            locationRepository.saveAndFlush(new LocationClass(Location.shelf.toString(), null));


            List<UnitOfQuantity> unitList = unitOfQuantityRepository.findAll();
            Map<String, UnitOfQuantity> mappedUnits = new HashMap<>();
            for (UnitOfQuantity unit :
                unitList) {
                mappedUnits.put(unit.getName(), unit);
            }

            // Items for fist Storage
            ItemStorage feta = new ItemStorage("Feta", null, null, null, 1, Location.fridge.toString(), mappedUnits.get("pieces"), ID_OF_STORAGE, null);
            LOGGER.debug("saving ItemStorage {}", feta);
            itemStorageRepository.save(feta);

            ItemStorage noodles = new ItemStorage("Noodles", null, null, null, 500, Location.shelf.toString(), mappedUnits.get("g"), ID_OF_STORAGE, null);
            LOGGER.debug("saving ItemStorage {}", noodles);
            itemStorageRepository.save(noodles);

            ItemStorage milk = new ItemStorage("Milk", null, null, null, 2, Location.fridge.toString(), mappedUnits.get("L"), ID_OF_STORAGE, null);
            LOGGER.debug("saving ItemStorage {}", milk);
            itemStorageRepository.save(milk);

            ItemStorage pesto = new ItemStorage("Pesto", "Genovese", null, null, 200, Location.fridge.toString(), mappedUnits.get("g"), ID_OF_STORAGE, null);
            LOGGER.debug("saving ItemStorage {}", pesto);
            itemStorageRepository.save(pesto);

            ItemStorage nutella = new ItemStorage("Nutella", "", null, null, 1, Location.fridge.toString(), mappedUnits.get("kg"), ID_OF_STORAGE, null);
            LOGGER.debug("saving ItemStorage {}", nutella);
            itemStorageRepository.save(nutella);

            ItemStorage eggs = new ItemStorage("Eggs", "", null, null, 2, Location.fridge.toString(), mappedUnits.get("pieces"), ID_OF_STORAGE, null);
            LOGGER.debug("saving ItemStorage {}", eggs);
            itemStorageRepository.save(eggs);

            ItemStorage soyamilk = new ItemStorage("Soya Milk", "", null, null, 1, Location.fridge.toString(), mappedUnits.get("L"), ID_OF_STORAGE, null);
            LOGGER.debug("saving ItemStorage {}", soyamilk);
            itemStorageRepository.save(soyamilk);

            ItemStorage onions = new ItemStorage("Pudding Mix", "", null, null, 5, Location.shelf.toString(), mappedUnits.get("pieces"), ID_OF_STORAGE, null);
            LOGGER.debug("saving ItemStorage {}", onions);
            itemStorageRepository.save(onions);

            ItemStorage butter = new ItemStorage("Butter", null, null, null,
                3, Location.fridge.toString(), mappedUnits.get("pieces"),
                ID_OF_STORAGE, null);
            LOGGER.debug("saving ItemStorage {}", butter);
            itemStorageRepository.save(butter);

            ItemStorage water = new ItemStorage("Water", null, null, null,
                2, Location.fridge.toString(), mappedUnits.get("L"),
                ID_OF_STORAGE, null);
            LOGGER.debug("saving ItemStorage {}", water);
            itemStorageRepository.save(water);

            ItemStorage orangeJuice = new ItemStorage("Orange Juice", null, null, null,
                1, Location.fridge.toString(), mappedUnits.get("L"),
                ID_OF_STORAGE, null);
            LOGGER.debug("saving ItemStorage {}", orangeJuice);
            itemStorageRepository.save(orangeJuice);

            ItemStorage gouda = new ItemStorage("Gouda", null, null, null,
                250, Location.fridge.toString(), mappedUnits.get("g"),
                ID_OF_STORAGE, null);
            LOGGER.debug("saving ItemStorage {}", orangeJuice);
            itemStorageRepository.save(gouda);

            ItemStorage sausages = new ItemStorage("sausages", null, null, null,
                5, Location.fridge.toString(), mappedUnits.get("pieces"),
                ID_OF_STORAGE, null);
            LOGGER.debug("saving ItemStorage {}", orangeJuice);
            itemStorageRepository.save(sausages);

            ItemStorage leftoverRice = new ItemStorage("Leftover Rice", "From 28.12.", null, null,
                150, Location.fridge.toString(), mappedUnits.get("g"),
                ID_OF_STORAGE, null);
            LOGGER.debug("saving ItemStorage {}", leftoverRice);
            itemStorageRepository.save(leftoverRice);

            ItemStorage greenPepper = new ItemStorage("Green Pepper", null, null, null,
                2, Location.fridge.toString(), mappedUnits.get("pieces"),
                ID_OF_STORAGE, null);
            LOGGER.debug("saving ItemStorage {}", greenPepper);
            itemStorageRepository.save(greenPepper);

            ItemStorage redPepper = new ItemStorage("Red Pepper", null, null, null,
                1, Location.fridge.toString(), mappedUnits.get("pieces"),
                ID_OF_STORAGE, null);
            LOGGER.debug("saving ItemStorage {}", redPepper);
            itemStorageRepository.save(redPepper);

            ItemStorage carrots = new ItemStorage("Carrots", null, null, null,
                200, Location.fridge.toString(), mappedUnits.get("g"),
                ID_OF_STORAGE, null);
            LOGGER.debug("saving ItemStorage {}", carrots);
            itemStorageRepository.save(carrots);

            ItemStorage saladHead = new ItemStorage("Salad Head", null, null, null,
                1, Location.fridge.toString(), mappedUnits.get("pieces"),
                ID_OF_STORAGE, null);
            LOGGER.debug("saving ItemStorage {}", saladHead);
            itemStorageRepository.save(saladHead);

            ItemStorage broccoli = new ItemStorage("Broccoli", null, null, null,
                150, Location.fridge.toString(), mappedUnits.get("g"),
                ID_OF_STORAGE, null);
            LOGGER.debug("saving ItemStorage {}", broccoli);
            itemStorageRepository.save(broccoli);

            ItemStorage tunaPizza = new ItemStorage("tunaPizza", null, null, null,
                1, Location.freezer.toString(), mappedUnits.get("pieces"),
                ID_OF_STORAGE, null);
            LOGGER.debug("saving ItemStorage {}", tunaPizza);
            itemStorageRepository.save(tunaPizza);

            ItemStorage fishFingers = new ItemStorage("Fish Fingers", null, null, null,
                250, Location.freezer.toString(), mappedUnits.get("g"),
                ID_OF_STORAGE, null);
            LOGGER.debug("saving ItemStorage {}", fishFingers);
            itemStorageRepository.save(fishFingers);

            ItemStorage iceCubes = new ItemStorage("Ice Cubes", null, null, null,
                100, Location.freezer.toString(), mappedUnits.get("g"),
                ID_OF_STORAGE, null);
            LOGGER.debug("saving ItemStorage {}", iceCubes);
            itemStorageRepository.save(iceCubes);

            ItemStorage fettuccine = new ItemStorage("Fettuccine", null, null, null,
                500, Location.shelf.toString(), mappedUnits.get("g"),
                ID_OF_STORAGE, null);
            LOGGER.debug("saving ItemStorage {}", fettuccine);
            itemStorageRepository.save(fettuccine);

            ItemStorage tomatoSauce = new ItemStorage("Tomato Sauce", null, null, null,
                3, Location.shelf.toString(), mappedUnits.get("can"),
                ID_OF_STORAGE, null);
            LOGGER.debug("saving ItemStorage {}", tomatoSauce);
            itemStorageRepository.save(tomatoSauce);

            ItemStorage pickles = new ItemStorage("Pickles", null, null, null,
                1, Location.shelf.toString(), mappedUnits.get("jar"),
                ID_OF_STORAGE, null);
            LOGGER.debug("saving ItemStorage {}", pickles);
            itemStorageRepository.save(pickles);

            ItemStorage apricotJar = new ItemStorage("Apricot Jar", null, null, null,
                2, Location.shelf.toString(), mappedUnits.get("jar"),
                ID_OF_STORAGE, null);
            LOGGER.debug("saving ItemStorage {}", apricotJar);
            itemStorageRepository.save(apricotJar);

            ItemStorage spaghetti = new ItemStorage("Spaghetti", null, null, null,
                430, Location.shelf.toString(), mappedUnits.get("g"),
                ID_OF_STORAGE, null);
            LOGGER.debug("saving ItemStorage {}", spaghetti);
            itemStorageRepository.save(spaghetti);

            ItemStorage tuna = new ItemStorage("Tuna", null, null, null,
                3, Location.shelf.toString(), mappedUnits.get("can"),
                ID_OF_STORAGE, null);
            LOGGER.debug("saving ItemStorage {}", tuna);
            itemStorageRepository.save(tuna);

            ItemStorage greenPesto = new ItemStorage("Green Pesto", null, null, null,
                350, Location.shelf.toString(), mappedUnits.get("g"),
                ID_OF_STORAGE, null);
            LOGGER.debug("saving ItemStorage {}", greenPesto);
            itemStorageRepository.save(greenPesto);

            ItemStorage redPesto = new ItemStorage("Red Pesto", null, null, null,
                150, Location.shelf.toString(), mappedUnits.get("g"),
                ID_OF_STORAGE, null);
            LOGGER.debug("saving ItemStorage {}", redPesto);
            itemStorageRepository.save(redPesto);


            // Items for second Storage
            ItemStorage feta2 = new ItemStorage("Feta", null, null, null, 1, Location.fridge.toString(), mappedUnits.get("pieces"), ID_OF_SECOND_STORAGE, null);
            LOGGER.debug("saving ItemStorage {}", feta2);
            itemStorageRepository.save(feta2);

            ItemStorage noodles2 = new ItemStorage("Noodles", null, null, null, 500, Location.shelf.toString(), mappedUnits.get("g"), ID_OF_SECOND_STORAGE, null);
            LOGGER.debug("saving ItemStorage {}", noodles2);
            itemStorageRepository.save(noodles2);

            ItemStorage milk2 = new ItemStorage("Milk", null, null, null, 2, Location.fridge.toString(), mappedUnits.get("L"), ID_OF_SECOND_STORAGE, null);
            LOGGER.debug("saving ItemStorage {}", milk2);
            itemStorageRepository.save(milk2);

            ItemStorage pesto2 = new ItemStorage("Pesto", "Genovese", null, null, 200, Location.fridge.toString(), mappedUnits.get("g"), ID_OF_SECOND_STORAGE, null);
            LOGGER.debug("saving ItemStorage {}", pesto2);
            itemStorageRepository.save(pesto2);

            // Items for fist shoppingList
            /*
            ItemStorage feta3 = new ItemStorage("Feta", null, null, null, 1, Location.fridge.toString(), mappedUnits.get("pieces"), null, ID_OF_SHOPPINGLIST);
            LOGGER.debug("saving ItemStorage {}", feta3);
            itemStorageRepository.save(feta3);

            ItemStorage noodles3 = new ItemStorage("Noodles", null, null, null, 500, Location.shelf.toString(), mappedUnits.get("g"), null, ID_OF_SHOPPINGLIST);
            LOGGER.debug("saving ItemStorage {}", noodles3);
            itemStorageRepository.save(noodles3);

            ItemStorage milk3 = new ItemStorage("Milk", null, null, null, 2, Location.fridge.toString(), mappedUnits.get("L"), null, ID_OF_SHOPPINGLIST);
            LOGGER.debug("saving ItemStorage {}", milk3);
            itemStorageRepository.save(milk3);

            ItemStorage pesto3 = new ItemStorage("Pesto", "Genovese", null, null, 200, Location.fridge.toString(), mappedUnits.get("g"), null, ID_OF_SHOPPINGLIST);
            LOGGER.debug("saving ItemStorage {}", pesto3);
            itemStorageRepository.save(pesto3);
*/

        } else {
            LOGGER.debug("generating {} ItemStorage entries", NUMBER_OF_ITEMSTORAGES);
            for (int i = 1; i <= NUMBER_OF_ITEMSTORAGES; i++) {

                ItemStorage itemStorage = new ItemStorage("name " + i, "notes for itemStorage " + i, null, null, 10, Location.fridge.toString(), null, null, null);
                LOGGER.debug("saving ItemStorage {}", itemStorage);
                itemStorageRepository.save(itemStorage);
            }
        }
    }
}
