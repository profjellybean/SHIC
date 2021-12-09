package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UnitOfQuantityDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UnitOfQuantityMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "/item")
public class ItemEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ItemService itemService;
    private final UnitOfQuantityMapper unitOfQuantityMapper;

    @Autowired
    public ItemEndpoint(ItemService itemService, UnitOfQuantityMapper unitOfQuantityMapper) {
        this.itemService = itemService;
        this.unitOfQuantityMapper = unitOfQuantityMapper;
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
    List<UnitOfQuantityDto> getAll() {
        LOGGER.info("getAllunitOfQuantity, itemEndpoint");
        return unitOfQuantityMapper.unitsOfQuantityToUnitsOfQuantityDto(itemService.getAll());
    }


}
