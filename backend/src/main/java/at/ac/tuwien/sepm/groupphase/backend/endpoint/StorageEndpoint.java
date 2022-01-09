package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemStorageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UnitOfQuantityDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ItemStorageMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.LocationMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UnitOfQuantityMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.UserGroup;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.GroupService;
import at.ac.tuwien.sepm.groupphase.backend.service.StorageService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/storage")
public class StorageEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final StorageService storageService;
    private final ItemStorageMapper itemStorageMapper;
    private final UnitOfQuantityMapper unitOfQuantityMapper;
    private final LocationMapper locationMapper;
    private final UserService userService;
    private final GroupService groupService;

    @Autowired
    public StorageEndpoint(StorageService storageService, ItemStorageMapper itemStorageMapper,
                           UnitOfQuantityMapper unitOfQuantityMapper, LocationMapper locationMapper, UserService userService,
                           GroupService groupService) {
        this.storageService = storageService;
        this.itemStorageMapper = itemStorageMapper;
        this.unitOfQuantityMapper = unitOfQuantityMapper;
        this.locationMapper = locationMapper;
        this.userService = userService;
        this.groupService = groupService;
    }


    @PostMapping
    @PermitAll
    @Operation(summary = "Insert a new item into the storage") //TODO: add security
    public ItemStorageDto saveItem(Authentication authentication, @Valid @RequestBody ItemStorageDto itemStorageDto) {
        LOGGER.info("POST /storage body: {}", itemStorageDto.toString());
        try {
            Long groupId = null;
            if (authentication != null) {
                groupId = userService.getGroupIdByUsername(authentication.getName());
            }
            return itemStorageMapper.itemStorageToItemStorageDto(storageService.saveItem(
                itemStorageMapper.itemStorageDtoToItemStorage(itemStorageDto), groupId));
        } catch (ServiceException s) {
            LOGGER.error(s.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping
    @PermitAll
    @Operation(summary = "Update an existing item of the storage") //TODO: add security
    public ItemStorageDto updateItem(Authentication authentication, @Valid @RequestBody ItemStorageDto itemStorageDto) {
        LOGGER.info("PUT /storage body: {}", itemStorageDto);
        try {
            Long groupId = null;
            if (authentication != null) {
                groupId = userService.getGroupIdByUsername(authentication.getName());
            }
            return itemStorageMapper.itemStorageToItemStorageDto(storageService.updateItem(
                itemStorageMapper.itemStorageDtoToItemStorage(itemStorageDto), groupId));
        } catch (ServiceException s) {
            LOGGER.error(s.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping
    @PermitAll
    @Operation(summary = "Get all items from the storage") //TODO: add security
    public List<ItemStorageDto> getAll(@Param("id") Long id) {
        LOGGER.info("getAll, endpoint");
        return itemStorageMapper.itemsStorageToItemsStorageDto(storageService.getAll(id));
    }

    @GetMapping(value = "/searchName")
    @PermitAll
    @Operation(summary = "Search for items from the storage by name") //TODO: add security
    public List<ItemStorageDto> searchItemName(@Param("id") Long id, @Param("name") String name) {
        LOGGER.info("searchItem, endpoint");
        return itemStorageMapper.itemsStorageToItemsStorageDto(storageService.searchItemName(id, name));
    }

    @GetMapping(value = "/search")
    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Search for items from storage by Dto")
    public List<ItemStorageDto> searchItem(@DateTimeFormat(pattern = "yyyy-MM-dd") ItemStorageDto itemStorageDto) {
        LOGGER.info("searchItem, endpoint");
        return itemStorageMapper.itemsStorageToItemsStorageDto(storageService.searchItem(itemStorageMapper.itemStorageDtoToItemStorage(itemStorageDto)));
    }

    @GetMapping(value = "/unitOfQuantity")
    @PermitAll
    @Operation(summary = "Get all units of quantity") //TODO: add security
    public List<UnitOfQuantityDto> getAllUnitsOfQuantity() {
        LOGGER.info("Get units of quantity, endpoint");
        return unitOfQuantityMapper.unitsOfQuantityToUnitsOfQuantityDto(storageService.getAllUnitOfQuantity());
    }

    @PermitAll
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public ItemStorageDto deleteItemFromStorage(Authentication authentication, @RequestParam Long itemId) {
        try {
            Long groupId = userService.getGroupIdByUsername(authentication.getName());
            UserGroup group = groupService.getOneById(groupId);
            return itemStorageMapper.itemStorageToItemStorageDto(storageService.deleteItemInStorageById(itemId, group.getStorageId()));

        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "/location")
    @PermitAll
    @Operation(summary = "Get all locations") //TODO: add security
    public List<LocationDto> getAllLocations() {
        try {
            LOGGER.info("getAllLocations, endpoint");
            return locationMapper.locationToLocationDto(storageService.getAllLocations());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "/locationWithStorageId")
    @PermitAll
    @Operation(summary = "Get all locations and storageId") //TODO: add security
    public List<LocationDto> getAllLocationsByStorageId(@Param("storageId") Long storageId) {
        try {
            LOGGER.info("getAllLocations by storageId, endpoint");
            return locationMapper.locationToLocationDto(storageService.getAllLocationsByStorageId(storageId));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "/locationWithNameandStorageId")
    @PermitAll
    @Operation(summary = "Get all locations by name and storageId") //TODO: add security
    public List<LocationDto> getAllLocationsByNameAndStorageId(@Param("name") String name, @Param("storageId") Long storageId) {
        try {
            LOGGER.info("getAllLocations by name and storageId, endpoint");
            return locationMapper.locationToLocationDto(storageService.getAllLocationsByNameAndStorageId(name, storageId));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping(value = "/location")
    @PermitAll
    @Operation(summary = "save location") //TODO: add security
    public void saveLocation(@Valid @RequestBody LocationDto locationDto) {
        try {
            LOGGER.info("saveLocation{}, endpoint", locationDto);
            storageService.saveLocation(locationMapper.locationDtoToLocation(locationDto));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (ServiceException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
