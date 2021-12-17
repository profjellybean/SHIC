package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UnitOfQuantityDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ItemMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ItemStorageMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UnitOfQuantityMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.ItemService;
import at.ac.tuwien.sepm.groupphase.backend.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/item")
public class ItemEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ItemService itemService;
    private final UnitOfQuantityMapper unitOfQuantityMapper;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemEndpoint(ItemService itemService, UnitOfQuantityMapper unitOfQuantityMapper, ItemMapper itemMapper) {
        this.itemService = itemService;
        this.unitOfQuantityMapper = unitOfQuantityMapper;
        this.itemMapper = itemMapper;
    }

    @PostMapping(value = "/unitOfQuantity")
    @PermitAll
    @Operation(summary = "create new Unit of Quantity")
    public UnitOfQuantityDto createUnitOfQuantity(@Valid @RequestBody UnitOfQuantityDto unitOfQuantityDto) {
        LOGGER.info("POST /unitOfQuantity: {}", unitOfQuantityDto.toString());
        return unitOfQuantityMapper.unitOfQuantityToUnitOfQuantityDto(itemService.addUnitOfQuantity(unitOfQuantityMapper.unitOfQuantityDtoToUnitOfQuantity(unitOfQuantityDto)));

    }

    @GetMapping(value = "/unitOfQuantity")
    @PermitAll
    @Operation(summary = "Get all Units of quantity")
    public List<UnitOfQuantityDto> getAll() {
        LOGGER.info("getAllunitOfQuantity, itemEndpoint");
        return unitOfQuantityMapper.unitsOfQuantityToUnitsOfQuantityDto(itemService.getAll());
    }

    @GetMapping //(value="/item")
    @PermitAll // TODO add security
    @Operation(summary = "Get all Items")
    List<ItemDto> getAllItems() {
        LOGGER.info("Endpoint: getAllItems()");
        return itemMapper.itemsToItemDtos(itemService.getAllItems());
    }

    @DeleteMapping
    @PermitAll
    @Operation(summary = "Deletes the item")
    public boolean deleteItem(@Valid @RequestBody ItemDto itemDto) {
        LOGGER.info("Delete item {}", itemDto.getName());
        return itemService.delete(itemMapper.itemDtoToItem(itemDto));
    }

}
