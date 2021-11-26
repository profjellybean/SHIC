package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.entity.Storage;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.StorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class StorageServiceImpl implements StorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final StorageRepository storageRepository;

    @Autowired
    public StorageServiceImpl(StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    @Override
    public Item deleteItemById(Long id) {
        LOGGER.debug("Delete item by id");
        try {
            Item itemToDelete = storageRepository.getById(id);
            storageRepository.delete(itemToDelete);
            return itemToDelete;
        } catch (NotFoundException e) {
            throw new NotFoundException();
        }
    }

    @Override
    public Item saveItem(Item item){
        LOGGER.debug("Save item");
        storageRepository.saveAndFlush(item);
        return item;
    }

    @Override
    public List<Item> getAll(){
        LOGGER.debug("Getting all items");
        return storageRepository.findAll();
    }
}
