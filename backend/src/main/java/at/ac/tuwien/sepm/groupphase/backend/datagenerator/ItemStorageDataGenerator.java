package at.ac.tuwien.sepm.groupphase.backend.datagenerator;


import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemStorageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;

//@Profile("generateData")
@Component
public class ItemStorageDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ItemStorageRepository itemStorageRepository;
    private static final int NUMBER_OF_ITEMSTORAGES = 5;
    private static final Long ID_OF_STORAGE = 1L;
    private static final boolean CREATE_REAL_ITEMSTORAGES = true;



    public ItemStorageDataGenerator(ItemStorageRepository itemStorageRepository) {
        this.itemStorageRepository = itemStorageRepository;
    }

    @PostConstruct
    void generateItemStorage() {
        if(itemStorageRepository.findAll().size() > 0) {
            LOGGER.debug("ItemStorage already generated");
        } else if(CREATE_REAL_ITEMSTORAGES) {

            ItemStorage feta = new ItemStorage("Feta", null, null, null, 1, Location.fridge, UnitOfQuantity.pieces, ID_OF_STORAGE);
            LOGGER.debug("saving ItemStorage {}", feta);
            itemStorageRepository.save(feta);

            ItemStorage noodles = new ItemStorage("Noodles", null, null, null, 500, Location.shelf, UnitOfQuantity.g, ID_OF_STORAGE);
            LOGGER.debug("saving ItemStorage {}", noodles);
            itemStorageRepository.save(noodles);

            ItemStorage milk = new ItemStorage("Milk", null, null, null, 2, Location.fridge, UnitOfQuantity.L, ID_OF_STORAGE);
            LOGGER.debug("saving ItemStorage {}", milk);
            itemStorageRepository.save(milk);

            ItemStorage pesto = new ItemStorage("Pesto", "Genovese", null, null, 200, Location.fridge, UnitOfQuantity.g, ID_OF_STORAGE);
            LOGGER.debug("saving ItemStorage {}", pesto);
            itemStorageRepository.save(pesto);



        } else {
            LOGGER.debug("generating {} ItemStorage entries", NUMBER_OF_ITEMSTORAGES);
            for (int i = 1; i <= NUMBER_OF_ITEMSTORAGES; i++) {

                ItemStorage itemStorage = new ItemStorage("name "+i, "notes for itemStorage "+i, null, null, 10, Location.fridge, null, null);
                //ItemStorage itemStorage = new ItemStorage((long) i,"name "+i, "notes for itemStorage "+i, null, null, 10, Location.fridge, UnitOfQuantity.kg, 1L);
                //ItemStorage itemStorage = new ItemStorage("name "+i, "notes for itemStorage "+i, null, null, 10, Location.fridge, UnitOfQuantity.kg, ID_OF_STORAGE);
                LOGGER.debug("saving ItemStorage {}", itemStorage);
                itemStorageRepository.save(itemStorage);
            }
        }
    }

}
