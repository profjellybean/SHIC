package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemStorageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShoppingListDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UsernameDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ItemStorageMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShoppingListCreationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShoppingListDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.WorkOffShoppingListDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ItemStorageMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ItemMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ItemStorageMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemStorageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ShoppingListMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.ShoppingListService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.core.Authentication;
import org.hibernate.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.lang.invoke.MethodHandles;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "/api/v1/shoppinglist")
public class ShoppingListEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ShoppingListService shoppingListService;
    private final ShoppingListMapper shoppingListMapper;
    private final UserService userService;
    private final ItemStorageMapper itemStorageMapper;
    private final ItemMapper itemMapper;

    @Autowired
    public ShoppingListEndpoint(ShoppingListService shoppingListService, ShoppingListMapper shoppingListMapper,
                                ItemStorageMapper itemStorageMapper, ItemMapper itemMapper, UserService userService) {
        this.shoppingListService = shoppingListService;
        this.shoppingListMapper = shoppingListMapper;
        this.itemStorageMapper = itemStorageMapper;
        this.itemMapper = itemMapper;
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

    @PostMapping("/newItem")
    @PermitAll
    @Operation(summary = "Insert a new item into the ShoppingList") //TODO: add security
    public ItemStorageDto saveItem(@RequestBody ItemStorageDto itemStorageDto) {
        LOGGER.info("Endpoint: POST /item to shopping list with id: {}", itemStorageDto.getStorageId());
        return itemStorageMapper.itemStorageToItemStorageDto(shoppingListService.saveItem(itemStorageMapper.itemStorageDtoToItemStorage(itemStorageDto), itemStorageDto.getShoppingListId()));
    }

    @PermitAll
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingListDto getShoppingListByid(@PathVariable Long id) {
        LOGGER.info("Endpoint: GET shoppingList by id: {}", id);
        try {
            return shoppingListMapper.shoppingListToShoppingListDto(shoppingListService.getShoppingListByid(id));

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); // Todo
        }


    }


    @GetMapping(value = "/availableItems")
    @PermitAll
    @Operation(summary = "Get all items from the shopping list") //TODO: add security
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
    public List<ItemStorageDto> planRecipe(Authentication authentication, @RequestParam(name = "recipeId") Long recipeId) {
        LOGGER.info("Endpoint: POST /api/v1/shoppinglist/recipeId={},userName={}", recipeId, authentication.getName());
        try {
            return itemStorageMapper.itemsStorageToItemsStorageDto(
                shoppingListService.planRecipe(recipeId, authentication));
        } catch (ValidationException e) {
            LOGGER.error("Error during planning recipe", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (NotFoundException e) {
            LOGGER.error("Error during planning recipe", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping(value = "/items")
    @PermitAll
    @Operation(summary = "Get list of all items") //TODO: add security
    public List<ItemDto> findAllItems() {
        LOGGER.info("GET /items");
        return itemMapper.itemsToItemDtos(shoppingListService.findAllItems());
    }


    @PermitAll
    @PostMapping("/private")
    @ResponseStatus(HttpStatus.OK)
    public ItemStorageDto addToPrivateShoppingListForUser(Authentication authentication, @RequestBody ItemStorageDto itemStorageDto) {
        try {
            Long id = userService.getPrivateShoppingListIdByUsername(authentication.getName());
            itemStorageDto.setShoppingListId(id);
            ItemStorage addedItem = shoppingListService.saveItem(itemStorageMapper.itemStorageDtoToItemStorage(itemStorageDto), id);
            return itemStorageMapper.itemStorageToItemStorageDto(addedItem);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); // Todo
        }

    }


    @PermitAll
    @PostMapping("/public")
    @ResponseStatus(HttpStatus.OK)
    public ItemStorageDto addToPublicShoppingListForUser(Authentication authentication, @RequestBody ItemStorageDto itemStorageDto) {

        try {
            Long id = userService.getPublicShoppingListIdByUsername(authentication.getName());
            itemStorageDto.setShoppingListId(id);

            return itemStorageMapper.itemStorageToItemStorageDto(shoppingListService.saveItem(itemStorageMapper.itemStorageDtoToItemStorage(itemStorageDto), id));

        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); // Todo
        }

    }

    @PermitAll
    @DeleteMapping("/public/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFromPublicShoppingListForUser(Authentication authentication, @PathVariable Long id) {
        try {
            Long shoppingListId = userService.getPublicShoppingListIdByUsername(authentication.getName());
            shoppingListService.deleteItemById(id, shoppingListId);

        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); // Todo
        }
    }

    @PermitAll
    @DeleteMapping("/private/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFromPrivateShoppingListForUser(Authentication authentication, @PathVariable Long id) {
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

    @PermitAll
    @GetMapping("/private")
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


    @PermitAll
    @GetMapping("/public")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingListDto getPublicShoppingListForUser(Authentication authentication) {
        try {
            return shoppingListMapper.shoppingListToShoppingListDto(userService.getPublicShoppingListByUsername(authentication.getName()));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); // Todo
        }

    }


    @PermitAll
    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemStorageDto> workOffShoppingList(Authentication authentication,
                                                    @RequestBody List<ItemStorageDto> boughtItems) {
        LOGGER.info("PUT workOffShoppingList{}", boughtItems);
        return itemStorageMapper.itemsStorageToItemsStorageDto(shoppingListService.workOffShoppingList(
            authentication, itemStorageMapper.itemsStorageDtoToItemsStorage(boughtItems)));
    }
}
