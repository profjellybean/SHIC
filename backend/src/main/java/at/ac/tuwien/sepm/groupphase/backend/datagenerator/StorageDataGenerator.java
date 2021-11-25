package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.repository.StorageRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

@Profile("generateData")
@Component
public class StorageDataGenerator {
    private static final Logger LOGGER= LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final StorageRepository storageRepository;

    public StorageDataGenerator(StorageRepository storageRepository){
        this.storageRepository=storageRepository;
    }
}
