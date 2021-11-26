package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ItemMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/storage")
public class StorageEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final StorageService storageService;
    private final ItemMapper itemMapper;

    @Autowired
    public StorageEndpoint(StorageService storageService, ItemMapper itemMapper) {
        this.storageService = storageService;
        this.itemMapper = itemMapper;
    }


    @PostMapping
    @PermitAll
    @Operation(summary = "Insert a new item into the storage") //TODO: add security
    public ItemDto saveItem(@Valid @RequestBody ItemDto itemDto) {
        LOGGER.info("POST /storage body: {}", itemDto.toString());

        return itemMapper.itemToItemDto(storageService.saveItem(itemMapper.itemDtoToItem(itemDto)));
    }

    @GetMapping
    @PermitAll
    @Operation(summary = "Get all items from the storage") //TODO: add security
    public List<ItemDto> getAll() {
        LOGGER.info("getAll, endpoint");
        return itemMapper.itemsToItemsDto(storageService.getAll());
    }
}
