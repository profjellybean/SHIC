package at.ac.tuwien.sepm.groupphase.backend.datagenerator;


import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.Location;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemStorageRepository;

import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.repository.UnitOfQuantityRepository;import org.slf4j.Logger;
import at.ac.tuwien.sepm.groupphase.backend.repository.StorageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Profile("generateData")
@Component
public class ItemStorageDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ItemStorageRepository itemStorageRepository;
    private final UnitOfQuantityRepository unitOfQuantityRepository;
    private static final int NUMBER_OF_ITEMSTORAGES = 5;
    private static final Long ID_OF_STORAGE = 1L;
    private static final Long ID_OF_SHOPPINGLIST = null;
    private static final boolean CREATE_REAL_ITEMSTORAGES = true;
    private final StorageRepository storageRepository;

    private final UnitOfQuantityDataGenerator unitOfQuantityDataGenerator;


    public ItemStorageDataGenerator(ItemStorageRepository itemStorageRepository,
                                    UnitOfQuantityRepository unitOfQuantityRepository,
                                    UnitOfQuantityDataGenerator unitOfQuantityDataGenerator,
                                    StorageRepository storageRepository) {
        this.itemStorageRepository = itemStorageRepository;
        this.unitOfQuantityRepository = unitOfQuantityRepository;
        this.unitOfQuantityDataGenerator = unitOfQuantityDataGenerator;

        this.storageRepository = storageRepository;
    }

    @PostConstruct
    void generateItemStorage() {
        if(itemStorageRepository.findAll().size() > 0) {
            LOGGER.debug("ItemStorage already generated");
        } else if(CREATE_REAL_ITEMSTORAGES) {
            LOGGER.debug("generating ItemStorage");

            this.unitOfQuantityDataGenerator.generateUnitOfQuantity();

            List<UnitOfQuantity> unitList = unitOfQuantityRepository.findAll();
            // System.out.println("\n\n\n"+"ALL UNITS:" + unitList+ "\n\n\n");
            Map<String, Long> mappedUnits = new HashMap<>();
            for (UnitOfQuantity unit :
                unitList) {
                mappedUnits.put(unit.getName(), unit.getId());
            }

            //ItemStorage feta = new ItemStorage("Feta", null, null, null, 1, Location.fridge, UnitOfQuantity.pieces, ID_OF_STORAGE);
            ItemStorage feta = new ItemStorage("Feta", null, null, null, 1, Location.fridge, mappedUnits.get("pieces"), ID_OF_STORAGE, ID_OF_SHOPPINGLIST);
            LOGGER.debug("saving ItemStorage {}", feta);
            itemStorageRepository.save(feta);

            //ItemStorage noodles = new ItemStorage("Noodles", null, null, null, 500, Location.shelf, UnitOfQuantity.g, ID_OF_STORAGE);
            ItemStorage noodles = new ItemStorage("Noodles", null, null, null, 500, Location.shelf, mappedUnits.get("g"), ID_OF_STORAGE, ID_OF_SHOPPINGLIST);
            LOGGER.debug("saving ItemStorage {}", noodles);
            itemStorageRepository.save(noodles);

            //ItemStorage milk = new ItemStorage("Milk", null, null, null, 2, Location.fridge, UnitOfQuantity.L, ID_OF_STORAGE);
            ItemStorage milk = new ItemStorage("Milk", null, null, null, 2, Location.fridge, mappedUnits.get("L"), ID_OF_STORAGE, ID_OF_SHOPPINGLIST);
            LOGGER.debug("saving ItemStorage {}", milk);
            itemStorageRepository.save(milk);

            //ItemStorage pesto = new ItemStorage("Pesto", "Genovese", null, null, 200, Location.fridge, UnitOfQuantity.g, ID_OF_STORAGE);
            ItemStorage pesto = new ItemStorage("Pesto", "Genovese", null, null, 200, Location.fridge, mappedUnits.get("g"), ID_OF_STORAGE, ID_OF_SHOPPINGLIST);
            LOGGER.debug("saving ItemStorage {}", pesto);
            itemStorageRepository.save(pesto);



        } else {
            LOGGER.debug("generating {} ItemStorage entries", NUMBER_OF_ITEMSTORAGES);
            for (int i = 1; i <= NUMBER_OF_ITEMSTORAGES; i++) {

                ItemStorage itemStorage = new ItemStorage("name "+i, "notes for itemStorage "+i, null, null, 10, Location.fridge, null, null, null);
                LOGGER.debug("saving ItemStorage {}", itemStorage);
                itemStorageRepository.save(itemStorage);
            }
        }
    }
}
