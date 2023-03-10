package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UnitOfQuantityDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UnitsRelationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ItemMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UnitOfQuantityMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UnitsRelationMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.ItemService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/item")
public class ItemEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ItemService itemService;
    private final UnitOfQuantityMapper unitOfQuantityMapper;
    private final UnitsRelationMapper unitsRelationMapper;
    private final ItemMapper itemMapper;
    private final UserService userService;

    @Autowired
    public ItemEndpoint(ItemService itemService, UnitOfQuantityMapper unitOfQuantityMapper, ItemMapper itemMapper,
                        UnitsRelationMapper unitsRelationMapper, UserService userService) {
        this.itemService = itemService;
        this.unitOfQuantityMapper = unitOfQuantityMapper;
        this.unitsRelationMapper = unitsRelationMapper;
        this.itemMapper = itemMapper;
        this.userService = userService;
    }

    @Secured("ROLE_USER")
    @PostMapping(value = "/unitOfQuantity")
    @Operation(summary = "create new Unit of Quantity")
    public UnitOfQuantityDto createUnitOfQuantity(Authentication authentication, @Param("name") String name) {
        LOGGER.info("POST /unitOfQuantity: {}", name);
        if (name == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name must not be empty");
        }
        if (name.trim().equals("")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name must not be empty");
        }
        Long groupId = null;
        if (authentication != null) {
            groupId = userService.getGroupIdByUsername(authentication.getName());
        }
        try {
            return unitOfQuantityMapper.unitOfQuantityToUnitOfQuantityDto(itemService.addUnitOfQuantity(unitOfQuantityMapper.unitOfQuantityDtoToUnitOfQuantity(new UnitOfQuantityDto(name, groupId))));

        } catch (ValidationException e) {
            LOGGER.error("Error while creating UnitOfQuantity: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }

    }

    @Secured("ROLE_USER")
    @PostMapping(value = "/unitsRelation")
    @Operation(summary = "create new Relation of Units")
    public UnitsRelationDto createUnitsRelation(@Valid @RequestBody UnitsRelationDto unitsRelationDto) {
        LOGGER.info("POST /unitsRelation: {}", unitsRelationDto.toString());
        return unitsRelationMapper.unitsRelationToUnitsRelationDto(itemService.addUnitsRelation(unitsRelationMapper.unitsRelationDtoToUnitsRelation(unitsRelationDto)));

    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/unitsRelation")
    @Operation(summary = "Get all Units of quantity")
    public List<UnitsRelationDto> getAllUnitsRelations() {
        LOGGER.info("getAllunitsRelations, itemEndpoint");
        return unitsRelationMapper.unitsRelationsToUnitsRelationsDto(itemService.getAllUnitsRelations());
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/unitsRelation/specificRelation")
    @Operation(summary = "Get specific Relation between Units")
    public UnitsRelationDto getSpecificUnitsRelations(@Param("baseUnit") String baseUnit, @Param("calculatedUnit") String calculatedUnit) {
        LOGGER.info("getSpecificUnitsRelation, itemEndpoint");
        return unitsRelationMapper.unitsRelationToUnitsRelationDto(itemService.getSpecificRelation(baseUnit, calculatedUnit));
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/unitOfQuantity")
    @Operation(summary = "Get all Units of quantity")
    public List<UnitOfQuantityDto> getAll() {
        LOGGER.info("getAllunitOfQuantity, itemEndpoint");
        return unitOfQuantityMapper.unitsOfQuantityToUnitsOfQuantityDto(itemService.getAll());
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/unitOfQuantity/forGroup")
    @Operation(summary = "Get all Units of quantity")
    public List<UnitOfQuantityDto> getAllByGroupId(Authentication authentication) {
        LOGGER.info("getAllunitOfQuantity, itemEndpoint");
        Long groupId = null;
        if (authentication != null) {
            groupId = userService.getGroupIdByUsername(authentication.getName());
        }
        try {
            return unitOfQuantityMapper.unitsOfQuantityToUnitsOfQuantityDto(itemService.getAllForGroup(groupId));
        } catch (ValidationException e) {
            LOGGER.error("Error while getting all Items for Group: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/unitOfQuantity/byId")
    @Operation(summary = "Get all Units of quantity")
    public String getOneUnitOfQuantity(@Param("id") Long id) {
        LOGGER.info("getOneUnitOfQuantity, itemEndpoint");
        return itemService.getUnitOfQuantityById(id);
    }

    @Secured("ROLE_USER")
    @DeleteMapping
    @Operation(summary = "Deletes the item")
    public boolean deleteItem(@Valid @RequestBody ItemDto itemDto) {
        LOGGER.info("Delete item {}", itemDto.getName());
        return itemService.delete(itemMapper.itemDtoToItem(itemDto));
    }

    @Secured("ROLE_USER")
    @GetMapping
    @Operation(summary = "Get all Items")
    List<ItemDto> getAllItems() {
        LOGGER.info("Endpoint: getAllItems()");
        return itemMapper.itemsToItemDtos(itemService.getAllItems());
    }

    @GetMapping("/groupItems")
    @Secured("ROLE_USER")
    @Operation(summary = "Get all available Items for specific Group")
    List<ItemDto> getAllItemsForGroup(Authentication authentication) {
        LOGGER.info("Endpoint: getAllItemsForGroup {}", authentication);
        try {
            return itemMapper.itemsToItemDtos(itemService.getAllItemsForGroupByUsername(authentication.getName()));
        } catch (ValidationException e) {
            LOGGER.error("Error while getting all Items for Group: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }
    }

    @GetMapping("/groupItemsByGroupId")
    @Secured("ROLE_USER")
    @Operation(summary = "Get all Items for specific Group by GroupId")
    List<ItemDto> getAllItemsByGroupId(Authentication authentication) {
        LOGGER.info("Endpoint: getAllItemsByGroupId {}", authentication);
        try {
            return itemMapper.itemsToItemDtos(itemService.findAllByGroupIdByUsername(authentication.getName()));
        } catch (ValidationException e) {
            LOGGER.error("Error while getting all Items for Group by groupId: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }
    }

    @GetMapping("/groupItemsByGroupIdAndName/{name}")
    @Secured("ROLE_USER")
    @Operation(summary = "Get all Items for specific Group by GroupId and by name")
    List<ItemDto> getAllItemsByGroupIdAndName(@PathVariable("name") String name, Authentication authentication) {
        LOGGER.info("Endpoint: getAllItemsByGroupIdAndName {}", name);
        LOGGER.info(name);
        try {
            return itemMapper.itemsToItemDtos(itemService.searchByName(name, authentication.getName()));
        } catch (ValidationException e) {
            LOGGER.error("Error while getting all Items for Group by groupId and name: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }
    }

    @PutMapping("/groupItems")
    @Secured("ROLE_USER")
    @Operation(summary = "Edit Item of a specific Group")
    ItemDto editCustomItem(Authentication authentication, @RequestBody ItemDto item) {
        LOGGER.info("Endpoint: editCustomItem {}{}", item, authentication);
        try {
            return itemMapper.itemToItemDto(itemService.editCustomItem(itemMapper.itemDtoToItem(item), authentication.getName()));
        } catch (ValidationException e) {
            LOGGER.error("Error while editing custom Item for Group: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }
    }

    @PostMapping("/groupItems")
    @Secured("ROLE_USER")
    @Operation(summary = "Add Item to Groups custom Items")
    ItemDto addCustomItem(Authentication authentication, @RequestBody ItemDto item) {
        LOGGER.info("Endpoint: addCustomItem {}{}", item, authentication);
        try {
            return itemMapper.itemToItemDto(itemService.addCustomItem(itemMapper.itemDtoToItem(item), authentication.getName()));
        } catch (ValidationException e) {
            LOGGER.error("Error while adding custom Item for Group: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }
    }

}
