package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.entity.Storage;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.StorageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.Set;

//@Profile("generateData")
@Component
public class StorageDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_STOREGES = 5;
    private static final String TEST_STORAGE_NAME = "NameStorage";
    private static final String TEST_STORAGE_NOTES = "Notes of the storage";
    private static final ApplicationUser TEST_STORAGE_OWNER = null;
    private static final Set<Item> TEST_STORAGE_ITEMS = null;

    private final StorageRepository storageRepository;
    private final LocationRepository locationRepository;

    public StorageDataGenerator(StorageRepository storageRepository, LocationRepository locationRepository) {
        this.storageRepository = storageRepository;
        this.locationRepository = locationRepository;
    }

    //@PostConstruct
    void generateStorage() {
        if (storageRepository.findAll().size() > 0) {
            LOGGER.debug("storage already generated");
        } else {
            LOGGER.debug("generating {} storage entries", NUMBER_OF_STOREGES);
            for (int i = 0; i < NUMBER_OF_STOREGES; i++) {
                Storage storage = new Storage();
                LOGGER.debug("saving Storage {}", storage);
                storageRepository.save(storage);
            }
        }
    }
}
