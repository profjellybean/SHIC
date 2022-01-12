package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.LocationClass;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.entity.Storage;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitsRelation;
import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.Location;

import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemStorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.StorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UnitsRelationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.StorageItemStorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserGroupRepository;

import at.ac.tuwien.sepm.groupphase.backend.repository.UnitOfQuantityRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ItemService;
import at.ac.tuwien.sepm.groupphase.backend.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StorageServiceImpl implements StorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final StorageRepository storageRepository;
    private final ItemStorageRepository itemStorageRepository;
    private final UnitOfQuantityRepository unitOfQuantityRepository;
    private final UnitsRelationRepository unitsRelationRepository;
    private final StorageItemStorageRepository storageItemStorageRepository;
    private final UserGroupRepository userGroupRepository;
    private final ItemService itemService;
    private final LocationRepository locationRepository;

    @Autowired
    public StorageServiceImpl(StorageRepository storageRepository,
                              ItemStorageRepository itemStorageRepository,
                              UnitOfQuantityRepository unitOfQuantityRepository,
                              UnitsRelationRepository unitsRelationRepository,
                              StorageItemStorageRepository storageItemStorageRepository,
                              UserGroupRepository userGroupRepository,
                              ItemService itemService,
                              LocationRepository locationRepository) {
        this.storageRepository = storageRepository;
        this.itemStorageRepository = itemStorageRepository;
        this.unitOfQuantityRepository = unitOfQuantityRepository;
        this.unitsRelationRepository = unitsRelationRepository;
        this.storageItemStorageRepository = storageItemStorageRepository;
        this.userGroupRepository = userGroupRepository;
        this.itemService = itemService;
        this.locationRepository = locationRepository;
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
    public ItemStorage deleteItemInStorageById(Long itemId, Long storageId) {
        LOGGER.debug("Delete item from storage {} {}", itemId, storageId);

        Optional<Storage> storageOptional = storageRepository.findById(storageId);
        Optional<ItemStorage> itemStorageOptional = itemStorageRepository.findById(itemId);

        if (storageOptional.isEmpty()) {
            throw new NotFoundException("No storage with this id found: " + storageId);
        } else if (itemStorageOptional.isEmpty()) {
            throw new NotFoundException("No item with this id found: " + itemId);
        } else {
            ItemStorage itemToDelete = itemStorageOptional.get();
            Storage storage = storageOptional.get();

            if (Objects.equals(itemToDelete.getStorageId(), storage.getId())) {
                //storageItemStorageRepository.deleteFromTable(storageId, itemId);
                itemStorageRepository.delete(itemToDelete);
            }
            return itemToDelete;
        }
    }

    @Override
    public ItemStorage saveItem(ItemStorage itemStorage, Long groupId) {
        LOGGER.debug("Service: Save item {} for group {}", itemStorage, groupId);

        if (itemStorage.getLocationTag() != null) {
            try {
                Location.valueOf(itemStorage.getLocationTag());
            } catch (IllegalArgumentException i) {
                throw new ValidationException("Location is not valid");
            }
        }

        itemService.checkForBluePrintForGroup(itemStorage, groupId);

        // check if there is already an item with the same name in the storage
        if (itemStorage.getStorageId() != null) {
            List<ItemStorage> itemsInStorage = itemStorageRepository.findAllByStorageId(itemStorage.getStorageId());
            Map<String, ItemStorage> storedItemsMap = itemsInStorage.stream()
                .collect(Collectors.toMap(ItemStorage::getName, Function.identity()));
            ItemStorage storedItem = storedItemsMap.get(itemStorage.getName());
            // if there is uch an item, compare the units of quantity
            if (storedItem != null && storedItem.getQuantity().equals(itemStorage.getQuantity())) {
                // if they are the same, add the amounts and save
                int newAmount = storedItem.getAmount() + itemStorage.getAmount();
                storedItem.setAmount(newAmount);
                return itemStorageRepository.saveAndFlush(storedItem);
            } else if (storedItem != null && storedItem.getQuantity() != null) {
                // else recalculate the amount, then add the amounts and save
                if (itemStorage.getQuantity() == null) {
                    throw new ValidationException("Unit of Quantity has to be set");
                }
                UnitsRelation unitsRelation = unitsRelationRepository.findUnitsRelationByBaseUnitAndCalculatedUnit(
                    storedItem.getQuantity().getName(), itemStorage.getQuantity().getName());
                if (unitsRelation != null) {
                    int newAmount = (int) (storedItem.getAmount() * unitsRelation.getRelation() + itemStorage.getAmount());
                    storedItem.setAmount(newAmount);
                    storedItem.setQuantity(itemStorage.getQuantity());
                    return itemStorageRepository.saveAndFlush(storedItem);
                } else {
                    throw new ServiceException("incompatible Units of Quantity");
                }
            }
        }

        return itemStorageRepository.saveAndFlush(itemStorage);
    }

    @Override
    public ItemStorage updateItem(ItemStorage itemStorage, Long groupId) {
        LOGGER.debug("Service: Update item {}", itemStorage);

        if (itemStorage.getLocationTag() != null) {
            try {
                Location.valueOf(itemStorage.getLocationTag());
            } catch (IllegalArgumentException i) {
                throw new ValidationException("Location is not valid");
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
    public List<UnitOfQuantity> getAllUnitOfQuantity() {
        LOGGER.debug("Getting all units of quantity");
        return unitOfQuantityRepository.findAll();
    }

    @Override
    public List<LocationClass> getAllLocations() {
        return locationRepository.findAll();
    }

    @Override
    public List<LocationClass> getAllLocationsByStorageId(Long storageId) {
        List<LocationClass> locationDefault = locationRepository.findAllByStorageId(null);
        List<LocationClass> locationStorage = locationRepository.findAllByStorageId(storageId);

        for (int i = 0; i < locationDefault.size(); i++) {
            locationStorage.add(locationDefault.get(i));
        }
        return locationStorage;
    }

    @Override
    public List<LocationClass> getAllLocationsByName(String name) {
        return locationRepository.findAllByName(name);
    }

    @Override
    public List<LocationClass> getAllLocationsByNameAndStorageId(String name, Long storageId) {
        return locationRepository.findAllByNameAndStorageId(name, storageId);
    }


    @Override
    public void saveLocation(LocationClass locationClass) {
        List<LocationClass> existingLocation = locationRepository.findAllByNameAndStorageId(locationClass.getName(), locationClass.getStorageId());
        if (!existingLocation.isEmpty()) {
            throw new ServiceException("location already exists!");
        }
        locationRepository.saveAndFlush(locationClass);
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
