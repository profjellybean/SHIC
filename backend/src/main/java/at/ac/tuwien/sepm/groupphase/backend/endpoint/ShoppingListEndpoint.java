package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.*;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ItemMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ItemStorageMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemStorageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ShoppingListMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.ShoppingListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.repository.query.Param;

import javax.annotation.security.PermitAll;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/shoppinglist")
public class ShoppingListEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ShoppingListService shoppingListService;
    private final ShoppingListMapper shoppingListMapper;
    private final ItemStorageMapper itemStorageMapper;
    private final ItemMapper itemMapper;

    @Autowired
    public ShoppingListEndpoint(ShoppingListService shoppingListService, ShoppingListMapper shoppingListMapper,
                                ItemStorageMapper itemStorageMapper, ItemMapper itemMapper) {
        this.shoppingListService = shoppingListService;
        this.shoppingListMapper = shoppingListMapper;
        this.itemStorageMapper = itemStorageMapper;
        this.itemMapper = itemMapper;
    }

    @PostMapping
    @PermitAll
    @Operation(summary = "Insert a new item into the storage") //TODO: add security
    public ItemStorageDto saveItem(@Valid @RequestBody ItemStorageDto itemStorageDto) {
        LOGGER.info("POST /item to shopping list: ", itemStorageDto.toString());
        return itemStorageMapper.itemStorageToItemStorageDto(shoppingListService.saveItem(itemStorageMapper.itemStorageDtoToItemStorage(itemStorageDto), itemStorageDto.getShoppingListId()));
    }

    @GetMapping
    @PermitAll
    @Operation(summary = "Get all items from the shopping list") //TODO: add security
    public List<ItemStorageDto> findAllByShoppingListId(@Param("id") Long id) {
        LOGGER.info("findAllByShoppingListId, endpoint");
        return itemStorageMapper.itemsStorageToItemsStorageDto(shoppingListService.findAllByShoppingListId(id));
    }

    @PermitAll //TODO: add security
    //@Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    @Operation(summary = "Plan a recipe: adds missing ingredients to shoppingList", security = @SecurityRequirement(name = "apiKey"))
    // TODO: change paramteters to new Dto?
    public List<ItemStorageDto> planRecipe(@RequestParam(name = "recipeId") Long recipeId, @RequestParam(name = "userId") Long userId) {
        LOGGER.info("Endpoint: POST /api/v1/shoppinglist/recipeId={},userId={}", recipeId, userId);
        return itemStorageMapper.itemsStorageToItemsStorageDto(
            shoppingListService.planRecipe(recipeId, userId));
    }

    @GetMapping(value= "/items")
    @PermitAll
    @Transactional
    @Operation(summary = "Get list of all items") //TODO: add security
    public List<ItemDto> findAllItems() {
        LOGGER.info("GET /items");
        return itemMapper.itemsToItemsDto(shoppingListService.findAllItems());
    }

}
