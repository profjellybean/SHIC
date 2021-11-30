package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemStorageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ItemStorageMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    private final ItemStorageMapper itemStorageMapper;

    @Autowired
    public StorageEndpoint(StorageService storageService, ItemStorageMapper itemStorageMapper) {
        this.storageService = storageService;
        this.itemStorageMapper = itemStorageMapper;
    }


    @PostMapping
    @PermitAll
    @Operation(summary = "Insert a new item into the storage") //TODO: add security
    public ItemStorageDto saveItem(@Valid @RequestBody ItemStorageDto itemStorageDto) {
        LOGGER.info("POST /storage body: {}", itemStorageDto.toString());

        return itemStorageMapper.itemStorageToItemStorageDto(storageService.saveItem(itemStorageMapper.itemStorageDtoToItemStorage(itemStorageDto)));
    }

    @GetMapping
    @PermitAll
    @Operation(summary = "Get all items from the storage") //TODO: add security
    public List<ItemStorageDto> getAll() {
        LOGGER.info("getAll, endpoint");
        return itemStorageMapper.itemsStorageToItemsStorageDto(storageService.getAll());
    }

    @GetMapping(value= "/search")
    @PermitAll
    @Operation(summary = "Search for items from the storage by name") //TODO: add security
    public List<ItemStorageDto> searchItem(@Valid @RequestBody String name) {
        LOGGER.info("searchItem, endpoint");
        return itemStorageMapper.itemsStorageToItemsStorageDto(storageService.searchItem(name));
    }
}
