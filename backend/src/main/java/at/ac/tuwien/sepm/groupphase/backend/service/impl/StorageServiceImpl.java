package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.Storage;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.StorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.StorageRepositoryStorage;
import at.ac.tuwien.sepm.groupphase.backend.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.web.Link;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.LinkedList;
import java.util.List;

@Service
public class StorageServiceImpl implements StorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final StorageRepository storageRepository;
    private final StorageRepositoryStorage storageRepositoryStorage;

    @Autowired
    public StorageServiceImpl(StorageRepository storageRepository, StorageRepositoryStorage storageRepositoryStorage) {
        this.storageRepository = storageRepository;
        this.storageRepositoryStorage = storageRepositoryStorage;
    }

    @Override
    public ItemStorage deleteItemById(Long id) {
        LOGGER.debug("Delete item by id");
        try {
            ItemStorage itemToDelete = storageRepository.getById(id);
            storageRepository.delete(itemToDelete);
            return itemToDelete;
        } catch (NotFoundException e) {
            throw new NotFoundException();
        }
    }

    @Override
    public ItemStorage saveItem(ItemStorage itemStorage, Long id){
        LOGGER.debug("Save item");
        if(findStorageById(id)!=null){
            storageRepository.saveAndFlush(itemStorage);
            storageRepository.insert(itemStorage.getId(), id);
        }
        else {
            Long newStorage = createNewStorage();
            storageRepository.saveAndFlush(itemStorage);
            storageRepository.insert(itemStorage.getId(),newStorage);
        }
        return itemStorage;
    }

    @Override
    public List<ItemStorage> getAll(Long id){
        LOGGER.debug("Getting all items");
        return storageRepository.findAllByStorageId(id);
    }

    @Override
    public Long findStorageById(Long id) {
        LOGGER.debug("Getting the Storage with the id");
        if(storageRepositoryStorage.findById(id).isPresent()){
            return id;
        }
        else {
            return null;
        }
    }

    @Override
    public Long createNewStorage() {
        LOGGER.debug("Creating a new storage");
        return storageRepositoryStorage.saveAndFlush(new Storage()).getId();
    }

    @Override
    public List<ItemStorage> searchItem(String name){
        LOGGER.debug("search for items");
        return storageRepository.findAllByNameLike(name);
    }

}
