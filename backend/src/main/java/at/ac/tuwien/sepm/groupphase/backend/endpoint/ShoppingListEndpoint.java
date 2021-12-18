package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemStorageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShoppingListCreationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShoppingListDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ItemStorageMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ShoppingListMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.ShoppingListService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.hibernate.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;
import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/shoppinglist")
public class ShoppingListEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ShoppingListService shoppingListService;
    private final ShoppingListMapper shoppingListMapper;
    private final UserService userService;
    private final ItemStorageMapper itemStorageMapper;

    @Autowired
    public ShoppingListEndpoint(ShoppingListService shoppingListService, ShoppingListMapper shoppingListMapper,
                                ItemStorageMapper itemStorageMapper, UserService userService) {

        this.shoppingListService = shoppingListService;
        this.shoppingListMapper = shoppingListMapper;
        this.itemStorageMapper = itemStorageMapper;
        this.userService = userService;
    }

    @PermitAll
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createShoppingList(@RequestBody ShoppingListCreationDto shoppingListCreationDto) {
        LOGGER.info("Endpoint: POST /shoppinglist");
        try {
            return shoppingListService.createNewShoppingList(shoppingListCreationDto);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); // Todo
        }

    }

    @PermitAll
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingListDto getShoppingListByid(@PathVariable Long id) {
        try {
            return shoppingListMapper.shoppingListToShoppingListDto(shoppingListService.getShoppingListByid(id));

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); // Todo
        }


    }

    /*
        @PostMapping
        @PermitAll
        @Operation(summary = "Insert a new item into the storage") //TODO: add security /// TEMPLATE
        public ItemStorageDto saveItem(@Valid @RequestBody ItemStorageDto itemStorageDto) {
            LOGGER.info("POST /storagy body: ", itemStorageDto.toString());
            return itemStorageMapper.itemStorageToItemStorageDto(shoppingListService.saveItem(itemStorageMapper.itemStorageDtoToItemStorage(itemStorageDto), itemStorageDto.getShoppingListId()));
        }

        @GetMapping
        @PermitAll
        @Operation(summary = "Get all items from the shopping list") //TODO: add security
        public List<ItemStorageDto> findAllByStorageId(@Param("id") Long id) {
            LOGGER.info("findAllByStorageId, endpoint");
            return itemStorageMapper.itemsStorageToItemsStorageDto(shoppingListService.findAllByStorageId(id));
        }

    */

    //@PermitAll
    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    @Operation(summary = "Plan a recipe: adds missing ingredients to shoppingList", security = @SecurityRequirement(name = "apiKey"))
    public List<ItemStorageDto> planRecipe(Authentication authentication, @RequestParam(name = "recipeId") Long recipeId) {
        LOGGER.info("Endpoint: POST /api/v1/shoppinglist/recipeId={},userName={}", recipeId, authentication.getName());
        try {
            return itemStorageMapper.itemsStorageToItemsStorageDto(
                shoppingListService.planRecipe(recipeId, authentication));
        } catch (ValidationException e) {
            LOGGER.error("Error during planning recipe", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (ObjectNotFoundException e) {
            LOGGER.error("Error during planning recipe", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }


    @PermitAll
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ShoppingListDto getPrivateShoppingListForUser(Authentication authentication) {
        try {


            Long id = userService.getPrivateShoppingListIdByUsername(authentication.getName());

            return shoppingListMapper.shoppingListToShoppingListDto(shoppingListService.getShoppingListByid(id));


        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); // Todo
        }

    }

}
