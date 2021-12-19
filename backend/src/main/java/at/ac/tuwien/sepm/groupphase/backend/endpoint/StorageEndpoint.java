package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemStorageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ItemStorageMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.Location;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.lang.invoke.MethodHandles;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/storage")
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
        try {
            return itemStorageMapper.itemStorageToItemStorageDto(storageService.saveItem(itemStorageMapper.itemStorageDtoToItemStorage(itemStorageDto)));
        } catch (ServiceException s) {
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
}
