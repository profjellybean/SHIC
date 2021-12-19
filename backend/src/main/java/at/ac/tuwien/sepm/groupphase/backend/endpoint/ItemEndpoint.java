package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UnitOfQuantityDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UnitsRelationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ItemMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UnitOfQuantityMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UnitsRelationMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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
    private final UnitsRelationMapper unitsRelationMapper;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemEndpoint(ItemService itemService, UnitOfQuantityMapper unitOfQuantityMapper, ItemMapper itemMapper, UnitsRelationMapper unitsRelationMapper) {
        this.itemService = itemService;
        this.unitOfQuantityMapper = unitOfQuantityMapper;
        this.unitsRelationMapper = unitsRelationMapper;
        this.itemMapper = itemMapper;
    }

    @PostMapping(value = "/unitOfQuantity")
    @PermitAll
    @Operation(summary = "create new Unit of Quantity")
    public UnitOfQuantityDto createUnitOfQuantity(@Valid @RequestBody UnitOfQuantityDto unitOfQuantityDto) {
        LOGGER.info("POST /unitOfQuantity: {}", unitOfQuantityDto.toString());
        return unitOfQuantityMapper.unitOfQuantityToUnitOfQuantityDto(itemService.addUnitOfQuantity(unitOfQuantityMapper.unitOfQuantityDtoToUnitOfQuantity(unitOfQuantityDto)));

    }

    @PostMapping(value = "/unitsRelation")
    @PermitAll
    @Operation(summary = "create new Relation of Units")
    public UnitsRelationDto createUnitsRelation(@Valid @RequestBody UnitsRelationDto unitsRelationDto) {
        LOGGER.info("POST /unitsRelation: {}", unitsRelationDto.toString());
        return unitsRelationMapper.unitsRelationToUnitsRelationDto(itemService.addUnitsRelation(unitsRelationMapper.unitsRelationDtoToUnitsRelation(unitsRelationDto)));

    }

    @GetMapping(value = "/unitsRelation")
    @PermitAll
    @Operation(summary = "Get all Units of quantity")
    public List<UnitsRelationDto> getAllUnitsRelations() {
        LOGGER.info("getAllunitsRelations, itemEndpoint");
        return unitsRelationMapper.unitsRelationsToUnitsRelationsDto(itemService.getAllUnitsRelations());
    }

    @GetMapping(value = "/unitsRelation/specificRelation")
    @PermitAll
    @Operation(summary = "Get specific Relation between Units")
    public UnitsRelationDto getSpecificUnitsRelations(@Param("baseUnit") String baseUnit, @Param("calculatedUnit") String calculatedUnit) {
        LOGGER.info("getSpecificUnitsRelation, itemEndpoint");
        return unitsRelationMapper.unitsRelationToUnitsRelationDto(itemService.getSpecificRelation(baseUnit, calculatedUnit));
    }


    @GetMapping(value = "/unitOfQuantity")
    @PermitAll
    @Operation(summary = "Get all Units of quantity")
    public List<UnitOfQuantityDto> getAll() {
        LOGGER.info("getAllunitOfQuantity, itemEndpoint");
        return unitOfQuantityMapper.unitsOfQuantityToUnitsOfQuantityDto(itemService.getAll());
    }

    @GetMapping(value = "/unitOfQuantity/byId")
    @PermitAll
    @Operation(summary = "Get all Units of quantity")
    public String getOneUnitOfQuantity(@Param("id") Long id) {
        LOGGER.info("getOneUnitOfQuantity, itemEndpoint");
        return itemService.getUnitOfQuantityById(id);
    }

    @DeleteMapping
    @PermitAll
    @Operation(summary = "Deletes the item")
    public boolean deleteItem(@Valid @RequestBody ItemDto itemDto) {
        LOGGER.info("Delete item {}", itemDto.getName());
        return itemService.delete(itemMapper.itemDtoToItem(itemDto));
    }


}
