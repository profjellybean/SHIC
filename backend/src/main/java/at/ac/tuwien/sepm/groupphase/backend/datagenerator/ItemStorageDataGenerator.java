package at.ac.tuwien.sepm.groupphase.backend.datagenerator;


import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemStorageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;

@Profile("generateData")
@Component
public class ItemStorageDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ItemStorageRepository itemStorageRepository;
    private static final int NUMBER_OF_ITEMSTORAGES = 5;


    public ItemStorageDataGenerator(ItemStorageRepository itemStorageRepository) {
        this.itemStorageRepository = itemStorageRepository;
    }

    @PostConstruct
    private void generateItemStorage() {
        if(itemStorageRepository.findAll().size() > 0) {
            LOGGER.debug("ItemStorage already generated");
        } else {
            LOGGER.debug("generating {} ItemStorage entries", NUMBER_OF_ITEMSTORAGES);
            for (int i = 0; i < NUMBER_OF_ITEMSTORAGES; i++) {

                ItemStorage itemStorage = new ItemStorage("name "+i, "notes for itemStorage "+i, null, null, 10, Location.fridge, UnitOfQuantity.kg, null);
                LOGGER.debug("saving ItemStorage {}", itemStorage);
                itemStorageRepository.save(itemStorage);
            }
        }
    }

}
