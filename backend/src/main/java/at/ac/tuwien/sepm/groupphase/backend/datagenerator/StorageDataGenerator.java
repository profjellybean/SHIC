package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import at.ac.tuwien.sepm.groupphase.backend.entity.Storage;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.StorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.StorageRepositoryStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.util.Set;

@Profile("generateData")
@Component
public class StorageDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_STOREGES = 5;
    private static final String TEST_STORAGE_NAME = "NameStorage";
    private static final String TEST_STORAGE_NOTES = "Notes of the storage";
    private static final ApplicationUser TEST_STORAGE_OWNER = null;
    private static final Set<Item> TEST_STORAGE_ITEMS = null;

    private final StorageRepositoryStorage storageRepositoryStorage;

    public StorageDataGenerator(StorageRepositoryStorage storageRepositoryStorage) {
        this.storageRepositoryStorage = storageRepositoryStorage;
    }

    @PostConstruct
    private void generateShoppingList() {
        if(storageRepositoryStorage.findAll().size() > 0) {
            LOGGER.debug("storage already generated");
        } else {
            LOGGER.debug("generating {} storage entries", NUMBER_OF_STOREGES);
            for (int i = 0; i < NUMBER_OF_STOREGES; i++) {
                // TODO add storages
                Storage storage = new Storage();
                storage.setId(0L);
                LOGGER.debug("saving storage {}", storage);
                storageRepositoryStorage.save(storage);

            }
        }
    }
}
