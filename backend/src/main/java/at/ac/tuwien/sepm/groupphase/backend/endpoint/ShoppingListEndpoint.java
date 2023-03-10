package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemStorageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShoppingListCreationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShoppingListDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ItemMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ItemStorageMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ShoppingListMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.ShoppingListService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.util.ItemStorageValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/shoppinglist")
public class ShoppingListEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ShoppingListService shoppingListService;
    private final ShoppingListMapper shoppingListMapper;
    private final UserService userService;
    private final ItemStorageMapper itemStorageMapper;
    private final ItemMapper itemMapper;
    private final ItemStorageValidator itemStorageValidator;

    @Autowired
    public ShoppingListEndpoint(ShoppingListService shoppingListService, ShoppingListMapper shoppingListMapper,
                                ItemStorageMapper itemStorageMapper, ItemMapper itemMapper, UserService userService, ItemStorageValidator itemStorageValidator) {
        this.shoppingListService = shoppingListService;
        this.shoppingListMapper = shoppingListMapper;
        this.itemStorageMapper = itemStorageMapper;
        this.itemMapper = itemMapper;
        this.userService = userService;
        this.itemStorageValidator = itemStorageValidator;
    }

    @Secured("ROLE_USER")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createShoppingList(@RequestBody ShoppingListCreationDto shoppingListCreationDto) {
        LOGGER.info("Endpoint: POST /shoppinglist");
        try {
            return shoppingListService.createNewShoppingList(shoppingListCreationDto);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    @Secured("ROLE_USER")
    @PostMapping("/newItem")
    @Operation(summary = "Insert a new item into the ShoppingList")
    public ItemStorageDto saveItem(Authentication authentication, @RequestBody ItemStorageDto itemStorageDto) {
        LOGGER.info("Endpoint: POST /item to shopping list with id: {}", itemStorageDto.getStorageId());
        try {
            itemStorageValidator.validateItemStorageDto(itemStorageDto);
            Long groupId = null;
            if (authentication != null) {
                groupId = userService.getGroupIdByUsername(authentication.getName());
            }
            return itemStorageMapper.itemStorageToItemStorageDto(shoppingListService.saveItem(
                itemStorageMapper.itemStorageDtoToItemStorage(itemStorageDto), itemStorageDto.getShoppingListId(), groupId));
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    @Secured("ROLE_USER")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingListDto getShoppingListByid(@PathVariable Long id) {
        LOGGER.info("Endpoint: GET shoppingList by id: {}", id);
        try {
            return shoppingListMapper.shoppingListToShoppingListDto(shoppingListService.getShoppingListByid(id));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/availableItems")
    @Operation(summary = "Get all items from the shopping list")
    public List<ItemStorageDto> getAvailableItemsForUser(Authentication authentication) {
        LOGGER.info("findAllByShoppingListId, endpoint");

        List<ItemStorage> fullList = userService.getCombinedAvailableItemsWithoutDuplicates(authentication.getName());
        List<ItemStorage> filteredList = new LinkedList<>();

        for (ItemStorage element : fullList) {
            if (!filteredList.contains(element)) {
                filteredList.add(element);
            }

        }
        return itemStorageMapper.itemsStorageToItemsStorageDto(filteredList);
    }


    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    @Operation(summary = "Plan a recipe: adds missing ingredients to shoppingList", security = @SecurityRequirement(name = "apiKey"))
    public List<ItemStorageDto> planRecipe(Authentication authentication,
                                           @RequestParam(name = "recipeId") Long recipeId, @RequestParam(name = "numberOfPeople") Integer numberOfPeople) {
        LOGGER.info("Endpoint: POST /shoppinglist/recipeId={},numberOfPeople={},userName={}", recipeId, numberOfPeople, authentication.getName());
        try {
            return itemStorageMapper.itemsStorageToItemsStorageDto(
                shoppingListService.planRecipe(recipeId, authentication.getName(), numberOfPeople));
        } catch (ValidationException e) {
            LOGGER.error("Error during planRecipe: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (NotFoundException e) {
            LOGGER.error("Error during planRecipe: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ServiceException e) {
            LOGGER.error("Error during planRecipe: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/putAllIngredientsOfRecipe")
    @Operation(summary = "Adds all ingredients of a recipe to shoppingList", security = @SecurityRequirement(name = "apiKey"))
    public List<ItemStorageDto> putRecipeOnShoppingList(Authentication authentication,
                                                        @RequestParam(name = "recipeId") Long recipeId, @RequestParam(name = "numberOfPeople") Integer people) {
        LOGGER.info("Endpoint: POST /shoppinglist/putAllIngredientsOfRecipe/recipeId={},numberOfPeople={},userName={}", recipeId, people, authentication.getName());
        try {
            return itemStorageMapper.itemsStorageToItemsStorageDto(
                shoppingListService.putRecipeOnShoppingList(recipeId, authentication.getName(), people));
        } catch (ValidationException e) {
            LOGGER.error("Error while putting all Ingredients of Recipe to ShoppingList: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (NotFoundException e) {
            LOGGER.error("Error while putting all Ingredients of Recipe to ShoppingList: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ServiceException e) {
            LOGGER.error("Error while putting all Ingredients of Recipe to ShoppingList: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/items")
    @Operation(summary = "Get list of all items")
    public List<ItemDto> findAllItems() {
        LOGGER.info("GET /items");
        return itemMapper.itemsToItemDtos(shoppingListService.findAllItems());
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/search")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Search for items from shopping list by Dto")
    public List<ItemStorageDto> searchItem(@DateTimeFormat(pattern = "yyyy-MM-dd") ItemStorageDto itemStorageDto) {
        LOGGER.info("searchItem, endpoint");
        return itemStorageMapper.itemsStorageToItemsStorageDto(shoppingListService.searchItem(itemStorageMapper.itemStorageDtoToItemStorage(itemStorageDto)));
    }

    @Secured("ROLE_USER")
    @PostMapping("/private")
    @ResponseStatus(HttpStatus.OK)
    public ItemStorageDto addToPrivateShoppingListForUser(Authentication authentication, @RequestBody ItemStorageDto itemStorageDto) {
        LOGGER.info("POST /private {}", itemStorageDto);
        try {
            itemStorageValidator.validateItemStorageDto(itemStorageDto);
            Long id = userService.getPrivateShoppingListIdByUsername(authentication.getName());
            itemStorageDto.setShoppingListId(id);
            Long groupId = userService.getGroupIdByUsername(authentication.getName());

            ItemStorage addedItem = shoppingListService.saveItem(itemStorageMapper.itemStorageDtoToItemStorage(itemStorageDto), id, groupId);
            return itemStorageMapper.itemStorageToItemStorageDto(addedItem);
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } catch (NotFoundException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    @Secured("ROLE_USER")
    @PostMapping("/public")
    @ResponseStatus(HttpStatus.OK)
    public ItemStorageDto addToPublicShoppingListForUser(Authentication authentication, @RequestBody ItemStorageDto itemStorageDto) {
        LOGGER.info("POST /public {}", itemStorageDto);
        try {
            itemStorageValidator.validateItemStorageDto(itemStorageDto);
            Long id = userService.getPublicShoppingListIdByUsername(authentication.getName());
            itemStorageDto.setShoppingListId(id);
            Long groupId = userService.getGroupIdByUsername(authentication.getName());

            return itemStorageMapper.itemStorageToItemStorageDto(shoppingListService.saveItem(itemStorageMapper.itemStorageDtoToItemStorage(itemStorageDto), id, groupId));

        } catch (ValidationException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } catch (NotFoundException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    @Secured("ROLE_USER")
    @PutMapping("/private/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemStorageDto changeAmountOfItemOnPrivateShoppingListForUser(Authentication authentication,
                                                                         @RequestBody ItemStorageDto itemStorageDto) {
        LOGGER.info("PUT /private/id {}", itemStorageDto);
        try {
            itemStorageValidator.validateItemStorageDto(itemStorageDto);
            Long shoppingListId = userService.getPrivateShoppingListIdByUsername(authentication.getName());

            return itemStorageMapper.itemStorageToItemStorageDto(shoppingListService.changeAmountOfItem(
                itemStorageMapper.itemStorageDtoToItemStorage(itemStorageDto), shoppingListId));

        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    @Secured("ROLE_USER")
    @PutMapping("/public/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemStorageDto changeAmountOfItemOnPublicShoppingListForUser(Authentication authentication,
                                                                        @RequestBody ItemStorageDto itemStorageDto) {
        LOGGER.info("PUT /public/id {}", itemStorageDto);
        try {
            itemStorageValidator.validateItemStorageDto(itemStorageDto);
            Long shoppingListId = userService.getPublicShoppingListIdByUsername(authentication.getName());

            return itemStorageMapper.itemStorageToItemStorageDto(shoppingListService.changeAmountOfItem(
                itemStorageMapper.itemStorageDtoToItemStorage(itemStorageDto), shoppingListId));

        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Secured("ROLE_USER")
    @DeleteMapping("/public/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFromPublicShoppingListForUser(Authentication authentication, @PathVariable Long id) {
        LOGGER.info("DELETE /public/id {}", id);
        try {
            Long shoppingListId = userService.getPublicShoppingListIdByUsername(authentication.getName());
            shoppingListService.deleteItemById(id, shoppingListId);

        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Secured("ROLE_USER")
    @DeleteMapping("/private/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFromPrivateShoppingListForUser(Authentication authentication, @PathVariable Long id) {
        LOGGER.info("DELETE /private/id {}", id);
        try {
            Long shoppingListId = userService.getPrivateShoppingListIdByUsername(authentication.getName());
            shoppingListService.deleteItemById(id, shoppingListId);

        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Secured("ROLE_USER")
    @GetMapping("/private")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingListDto getPrivateShoppingListForUser(Authentication authentication) {
        try {


            Long id = userService.getPrivateShoppingListIdByUsername(authentication.getName());

            return shoppingListMapper.shoppingListToShoppingListDto(shoppingListService.getShoppingListByid(id));


        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }


    @Secured("ROLE_USER")
    @GetMapping("/public")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingListDto getPublicShoppingListForUser(Authentication authentication) {
        try {
            return shoppingListMapper.shoppingListToShoppingListDto(userService.getPublicShoppingListByUsername(authentication.getName()));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }


    @Secured("ROLE_USER")
    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemStorageDto> workOffShoppingList(Authentication authentication,
                                                    @RequestBody List<ItemStorageDto> boughtItems) {
        LOGGER.info("PUT workOffShoppingList{}", boughtItems);
        return itemStorageMapper.itemsStorageToItemsStorageDto(shoppingListService.workOffShoppingList(
            authentication, itemStorageMapper.itemsStorageDtoToItemsStorage(boughtItems)));
    }
}
