package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShoppingListCreationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShoppingListDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.MessageMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ShoppingListMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.PasswordTooShortException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UsernameTakenException;
import at.ac.tuwien.sepm.groupphase.backend.service.MessageService;
import at.ac.tuwien.sepm.groupphase.backend.service.ShoppingListService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;
import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping(value = "/shoppinglist")
public class ShoppingListEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ShoppingListService shoppingListService;
    private final ShoppingListMapper shoppingListMapper;
    private final UserService userService;
    @Autowired
    public ShoppingListEndpoint(ShoppingListService shoppingListService, ShoppingListMapper shoppingListMapper,UserService userService) {
        this.shoppingListService = shoppingListService;
        this.shoppingListMapper = shoppingListMapper;

        this.userService = userService;
    }

    @PermitAll
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createShoppingList(@RequestBody ShoppingListCreationDto shoppingListCreationDto){
        LOGGER.info("Endpoint: POST /shoppinglist");
        try {
            return shoppingListService.createNewShoppingList(shoppingListCreationDto);

        }catch (Exception e) {
           throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage()); // Todo
        }

    }

    @PermitAll
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingListDto getShoppingListByid(@PathVariable Long id){
        try {
            return shoppingListMapper.shoppingListToShoppingListDto(shoppingListService.getShoppingListByid(id));

        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage()); // Todo
        }

    }

    @PermitAll
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ShoppingListDto getPrivateShoppingListForUser(Authentication authentication){
        try {


            Long id = userService.getPrivateShoppingListIdByUsername(authentication.getName());

            return shoppingListMapper.shoppingListToShoppingListDto(shoppingListService.getShoppingListByid(id));


        }catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage()); // Todo
        }

    }

}
