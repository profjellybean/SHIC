package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.Storage;
import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.Location;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
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
    public ItemStorage saveItem(ItemStorage itemStorage) {
        LOGGER.debug("Save item");
        if (itemStorage.getLocationTag() != null) {
            try {
                Location.valueOf(itemStorage.getLocationTag());
            } catch (IllegalArgumentException i) {
                throw new ServiceException("Location is not valid");
            }
        }

        return itemStorageRepository.saveAndFlush(itemStorage);
    }

    @Override
    public List<ItemStorage> searchItem(ItemStorage itemStorage) {
        LOGGER.info("Search for Items by ItemStorage {}", itemStorage);
        if (itemStorage.getNotes() != null) {
            if (itemStorage.getNotes().trim().equals("")) {
                itemStorage.setNotes(null);
            }
        }
        if (itemStorage.getName() != null) {
            if (itemStorage.getName().trim().equals("")) {
                itemStorage.setName(null);
            }
        }
        if (itemStorage.getLocationTag() != null) {
            if (itemStorage.getLocationTag().trim().equals("")) {
                itemStorage.setLocationTag(null);
            }
        }

        return itemStorageRepository.findAllByItemStorage(
            itemStorage.getStorageId(), itemStorage.getAmount(), itemStorage.getLocationTag() == null ? null : itemStorage.getLocationTag(),
            itemStorage.getName() == null ? null : "%" + itemStorage.getName() + "%",
            itemStorage.getNotes() == null ? null : "%" + itemStorage.getNotes() + "%",
            itemStorage.getExpDate());

    }


    @Override
    public List<ItemStorage> deleteItemsWhichDoNotExists(List<ItemStorage> itemStoragesAll, List<ItemStorage> itemStoragesFilter) {
        itemStoragesAll.removeIf(i -> !itemStoragesFilter.contains(i));
        return itemStoragesAll;
    }

    @Override
    public List<ItemStorage> getAll(Long id) {
        LOGGER.debug("Getting all items");
        return itemStorageRepository.findAllByStorageId(id);
    }

    @Override
    public Long findStorageById(Long id) {
        LOGGER.debug("Getting the Storage with the id");
        if (storageRepository.findById(id).isPresent()) {
            return id;
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public Long createNewStorage() {
        LOGGER.debug("Creating a new storage");
        return storageRepository.saveAndFlush(new Storage()).getId();
    }

    @Override
    public List<ItemStorage> searchItemName(Long id, String name) {
        LOGGER.debug("search for items");
        return itemStorageRepository.findAllByStorageIdAndNameContainingIgnoreCase(id, name);
    }


}
