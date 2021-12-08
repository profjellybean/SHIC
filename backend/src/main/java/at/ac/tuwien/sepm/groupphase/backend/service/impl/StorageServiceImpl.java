package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.Storage;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemStorageRepository;
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
    private final ItemStorageRepository itemStorageRepository;

    @Autowired
    public StorageServiceImpl(StorageRepository storageRepository, ItemStorageRepository itemStorageRepository) {
        this.storageRepository = storageRepository;
        this.itemStorageRepository = itemStorageRepository;
    }

    @Override
    public ItemStorage deleteItemById(Long id) {
        LOGGER.debug("Delete item by id");
        try {
            ItemStorage itemToDelete = itemStorageRepository.getById(id);
            itemStorageRepository.delete(itemToDelete);
            return itemToDelete;
        } catch (NotFoundException e) {
            throw new NotFoundException();
        }
    }

    @Override
    public ItemStorage saveItem(ItemStorage itemStorage){
        LOGGER.debug("Save item");
        return itemStorageRepository.saveAndFlush(itemStorage);
    }

    @Override
    public List<ItemStorage> getAll(Long id){
        LOGGER.debug("Getting all items");
        return itemStorageRepository.findAllByStorageId(id);
    }

    @Override
    public Long findStorageById(Long id) {
        LOGGER.debug("Getting the Storage with the id");
        if(storageRepository.findById(id).isPresent()){
            return id;
        }
        else {
            return null;
        }
    }

    @Override
    public Long createNewStorage() {
        LOGGER.debug("Creating a new storage");
        return storageRepository.saveAndFlush(new Storage()).getId();
    }

    @Override
    public List<ItemStorage> searchItem(Long id, String name){
        LOGGER.debug("search for items");
        return itemStorageRepository.findAllByStorageIdAndNameContainingIgnoreCase(id,name);
    }

}
