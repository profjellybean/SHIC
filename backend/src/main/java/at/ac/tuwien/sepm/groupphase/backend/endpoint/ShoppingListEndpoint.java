package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedMessageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.MessageInquiryDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShoppingListDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.MessageMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ShoppingListMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import at.ac.tuwien.sepm.groupphase.backend.service.MessageService;
import at.ac.tuwien.sepm.groupphase.backend.service.ShoppingListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.invoke.MethodHandles;

@RestController
// TODO: change value to "api/v1/shoppinglist" ?
@RequestMapping(value = "/shoppinglist")
public class ShoppingListEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ShoppingListService shoppingListService;
    private final ShoppingListMapper shoppingListMapper;

    @Autowired
    public ShoppingListEndpoint(ShoppingListService shoppingListService, ShoppingListMapper shoppingListMapper) {
        this.shoppingListService = shoppingListService;
        this.shoppingListMapper = shoppingListMapper;
    }


    //@Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/{recipeId},{storageId}")
    @Operation(summary = "Plan a recipe: creates shoppingList based on missing ingredients", security = @SecurityRequirement(name = "apiKey"))
    // TODO: change paramteters to new Dto
    public ShoppingListDto create(@PathVariable Long recipeId, @PathVariable Long storageId) {
        LOGGER.info("Endpoint: POST /shoppinglist/{},{}", recipeId, storageId);
        return shoppingListMapper.shoppingListToShoppingListDto(
            shoppingListService.planRecipe(recipeId, storageId));
    }

}
