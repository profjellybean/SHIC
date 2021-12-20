package at.ac.tuwien.sepm.groupphase.backend.datagenerator;


import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.Location;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemStorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.StorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UnitOfQuantityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Profile("generateData")
@Component
public class ItemStorageDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ItemStorageRepository itemStorageRepository;
    private final UnitOfQuantityRepository unitOfQuantityRepository;
    private static final int NUMBER_OF_ITEMSTORAGES = 5;
    private static final Long ID_OF_STORAGE = 6L;
    private static final Long ID_OF_SECOND_STORAGE = 1L;
    private static final Long ID_OF_SHOPPINGLIST = 1L;
    private static final boolean CREATE_REAL_ITEMSTORAGES = true;
    private final StorageDataGenerator storageDataGenerator;
    private final UserDataGenerator userDataGenerator;

    private final UnitOfQuantityDataGenerator unitOfQuantityDataGenerator;


    public ItemStorageDataGenerator(ItemStorageRepository itemStorageRepository,
                                    UnitOfQuantityRepository unitOfQuantityRepository,
                                    UnitOfQuantityDataGenerator unitOfQuantityDataGenerator,
                                    StorageDataGenerator storageDataGenerator,
                                    UserDataGenerator userDataGenerator) {
        this.itemStorageRepository = itemStorageRepository;
        this.unitOfQuantityRepository = unitOfQuantityRepository;
        this.unitOfQuantityDataGenerator = unitOfQuantityDataGenerator;

        this.storageDataGenerator = storageDataGenerator;
        this.userDataGenerator = userDataGenerator;
    }

    //@PostConstruct
    void generateItemStorage() {
        if (itemStorageRepository.findAll().size() > 0) {
            LOGGER.debug("ItemStorage already generated");
        } else if (CREATE_REAL_ITEMSTORAGES) {
            LOGGER.debug("generating ItemStorage");

            this.storageDataGenerator.generateStorage();
            this.unitOfQuantityDataGenerator.generateUnitOfQuantity();
            this.userDataGenerator.generateUser();

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
