package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Storage;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.invoke.MethodHandles;

public class StorageServiceImpl implements StorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final Storage storage;

    @Autowired
    public StorageServiceImpl(Storage storage) {
        this.storage = storage;
    }

    @Override
    public void deleteItemById(Long id) {
        LOGGER.debug("Delete item by id");
        try{
            storage.getItems();
        } catch (NotFoundException e) {

        }
    }
}
