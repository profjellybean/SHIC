package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemStorageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NameSumDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TimeSumDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UnitOfQuantityDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ItemStorageMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.LocationMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UnitOfQuantityMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.TrashOrUsedItem;
import at.ac.tuwien.sepm.groupphase.backend.entity.UserGroup;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.exception.TooFewIngredientsException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.GroupService;
import at.ac.tuwien.sepm.groupphase.backend.service.StorageService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
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
    @Secured("ROLE_USER")
    @Operation(summary = "Insert a new item into the storage")
    public ItemStorageDto saveItem(Authentication authentication, @Valid @RequestBody ItemStorageDto itemStorageDto) {
        LOGGER.info("Endpoint: POST /storage saveItem {} for user {}", itemStorageDto, authentication.getName());
        try {
            return itemStorageMapper.itemStorageToItemStorageDto(storageService.saveItemByUsername(
                itemStorageMapper.itemStorageDtoToItemStorage(itemStorageDto), authentication.getName()));
        } catch (ServiceException e) {
            LOGGER.error("Error while saving Item to Storage: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        } catch (ValidationException e) {
            LOGGER.error("Error while saving Item to Storage: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/recipe")
    @Operation(summary = "Cook a recipe: deleted ingredients of recipe from storage", security = @SecurityRequirement(name = "apiKey"))
    public List<ItemStorageDto> cookRecipe(Authentication authentication, @RequestParam(name = "recipeId") Long recipeId, @RequestParam(name = "numberOfPeople") Integer numberOfPeople) {
        LOGGER.info("Endpoint: POST /storage/recipeId={},numberOfPeople={},userName={}", recipeId, numberOfPeople, authentication.getName());
        try {
            return itemStorageMapper.itemsStorageToItemsStorageDto(
                storageService.cookRecipe(recipeId, authentication.getName(), numberOfPeople));
        } catch (ValidationException e) {
            LOGGER.error("Error during planRecipe: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (NotFoundException e) {
            LOGGER.error("Error during planRecipe: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (TooFewIngredientsException e) {
            LOGGER.error("Error during planRecipe: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, e.getMessage());
        } catch (ServiceException e) {
            LOGGER.error("Error during planRecipe: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    @Secured("ROLE_USER")
    @PutMapping
    @Operation(summary = "Update an existing item of the storage")
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

    @Secured("ROLE_USER")
    @GetMapping
    @Operation(summary = "Get all items from the storage")
    public List<ItemStorageDto> getAll(@Param("id") Long id) {
        LOGGER.info("getAll, endpoint");
        return itemStorageMapper.itemsStorageToItemsStorageDto(storageService.getAll(id));
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/searchName")
    @Operation(summary = "Search for items from the storage by name")
    public List<ItemStorageDto> searchItemName(@Param("id") Long id, @Param("name") String name) {
        LOGGER.info("searchItem, endpoint");
        return itemStorageMapper.itemsStorageToItemsStorageDto(storageService.searchItemName(id, name));
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/search")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Search for items from storage by Dto")
    public List<ItemStorageDto> searchItem(@DateTimeFormat(pattern = "yyyy-MM-dd") ItemStorageDto itemStorageDto) {
        LOGGER.info("searchItem, endpoint");
        return itemStorageMapper.itemsStorageToItemsStorageDto(storageService.searchItem(itemStorageMapper.itemStorageDtoToItemStorage(itemStorageDto)));
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/unitOfQuantity")
    @Operation(summary = "Get all units of quantity")
    public List<UnitOfQuantityDto> getAllUnitsOfQuantity(Authentication authentication) {
        LOGGER.info("Get units of quantity, endpoint");
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not logged-in");
        }
        Long groupId = userService.getGroupIdByUsername(authentication.getName());

        return unitOfQuantityMapper.unitsOfQuantityToUnitsOfQuantityDto(storageService.getAllUnitOfQuantity(groupId));
    }

    @Secured("ROLE_USER")
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public ItemStorageDto deleteItemFromStorage(Authentication authentication, @RequestParam Long itemId, boolean trash) {
        try {
            Long groupId = userService.getGroupIdByUsername(authentication.getName());
            UserGroup group = groupService.getOneById(groupId);
            return itemStorageMapper.itemStorageToItemStorageDto(storageService.deleteItemInStorageById(itemId, group.getStorageId(), trash));

        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/thrownAwayInSpecificMonth")
    @ResponseStatus(HttpStatus.OK)
    public TimeSumDto sumOfArticlesOfSpecificMonth(Authentication authentication, @Param("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        LOGGER.info("Endpoint: GET /api/v1/storage/{}", authentication);
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not logged-in");
        }
        if (date == null) {
            return null;
        }
        return new TimeSumDto(storageService.sumOfArticlesOfSpecificMonth(authentication.getName(), date), date);
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/thrownAwayInSpecificYear")
    @ResponseStatus(HttpStatus.OK)
    public TimeSumDto sumOfArticlesOfSpecificYear(Authentication authentication, @Param("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        LOGGER.info("Endpoint: GET /api/v1/storage/{}", authentication);
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not logged-in");
        }
        if (date == null) {
            return null;
        }
        return new TimeSumDto(storageService.sumOfArticlesOfSpecificYear(authentication.getName(), date), date);
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/mostOftenThrownAwayArticles")
    @ResponseStatus(HttpStatus.OK)
    public NameSumDto[] mostOftenThrownAwayArticles(Authentication authentication) {
        LOGGER.info("Endpoint: GET /api/v1/storage/{}", authentication);
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not logged-in");
        }
        List<TrashOrUsedItem> trashOrUsedItems = storageService.getMostThrownAwayArticles(authentication.getName());
        NameSumDto[] nameSumDtos = new NameSumDto[10];
        int sum = 0;
        for (TrashOrUsedItem i : trashOrUsedItems) {
            nameSumDtos[sum] = new NameSumDto(i.getItemName(), i.getAmount());
            sum++;
        }
        return nameSumDtos;

    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/location")
    @Operation(summary = "Get all locations")
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

    @Secured("ROLE_USER")
    @GetMapping(value = "/locationWithStorageId")
    @Operation(summary = "Get all locations and storageId")
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

    @Secured("ROLE_USER")
    @GetMapping(value = "/locationWithNameandStorageId")
    @Operation(summary = "Get all locations by name and storageId")
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

    @Secured("ROLE_USER")
    @PostMapping(value = "/location")
    @Operation(summary = "save location")
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

    @Secured("ROLE_USER")
    @DeleteMapping(value = "/location")
    @Operation(summary = "delete location")
    public void deleteLocation(@Valid @Param("id") Long id) {
        try {
            LOGGER.info("deleteLocation{}, endpoint", id);
            storageService.deleteLocation(id);
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
