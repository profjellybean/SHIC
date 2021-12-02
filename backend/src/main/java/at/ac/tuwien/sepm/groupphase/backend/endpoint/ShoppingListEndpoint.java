package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemStorageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShoppingListDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ItemStorageMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ShoppingListMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.ShoppingListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
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

    @Autowired
    public ShoppingListEndpoint(ShoppingListService shoppingListService, ShoppingListMapper shoppingListMapper,
                                ItemStorageMapper itemStorageMapper) {
        this.shoppingListService = shoppingListService;
        this.shoppingListMapper = shoppingListMapper;
        this.itemStorageMapper = itemStorageMapper;
    }

    @PermitAll
    //@Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    @Operation(summary = "Plan a recipe: creates shoppingList based on missing ingredients", security = @SecurityRequirement(name = "apiKey"))
    // TODO: change paramteters to new Dto?
    public List<ItemStorageDto> create(@RequestParam(name = "recipeId") Long recipeId, @RequestParam(name = "storageId") Long storageId) {
        LOGGER.info("Endpoint: POST /api/v1/shoppinglist/recipeId={},storageId={}", recipeId, storageId);
        return itemStorageMapper.itemsStorageToItemsStorageDto(
            shoppingListService.planRecipe(recipeId, storageId));
    }

}
