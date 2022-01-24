package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RecipeDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.RecipeMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnchangeableException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.RecipeService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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

import javax.annotation.security.PermitAll;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/recipe")
public class RecipeEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final RecipeMapper recipeMapper;
    private final RecipeService recipeService;
    private final UserService userService;

    @Autowired
    public RecipeEndpoint(RecipeMapper recipeMapper, RecipeService recipeService, UserService userService) {
        this.recipeMapper = recipeMapper;
        this.recipeService = recipeService;
        this.userService = userService;
    }

    @Secured("ROLE_USER")
    @GetMapping
    @Operation(summary = "Get list of all recipes")
    public List<RecipeDto> findAll() {
        LOGGER.info("GET /recipe");
        return recipeMapper.recipeToRecipeDto(recipeService.findAll());
    }

    @GetMapping("/findbyname")
    @Secured("ROLE_USER")
    @Operation(summary = "Get list of all recipes that contain the String name")
    public List<RecipeDto> findAllBySubstring(@RequestParam(name = "name") String name) {
        LOGGER.info("ENDPOINT: GET /recipe/findbyname");
        return recipeMapper.recipeToRecipeDto(recipeService.findRecipeBySubstring(name));
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/{id}")
    @Operation(summary = "Get recipe by id")
    public RecipeDto findRecipeById(@PathVariable("id") Long id) {
        LOGGER.info("GET /recipe by id");
        return recipeMapper.recipeToRecipeDto(recipeService.findRecipeById(id));
    }

    @Secured("ROLE_USER")
    @PostMapping
    @Operation(summary = "Add a new Recipe")
    public RecipeDto addRecipe(@RequestBody RecipeDto recipeDto) {
        LOGGER.info("Add recipe {}", recipeDto.getName());
        return recipeMapper.recipeToRecipeDto(recipeService.addRecipe(recipeMapper.recipeDtoToRecipe(recipeDto)));
    }

    @Secured("ROLE_USER")
    @PutMapping(value = "/{id}")
    @Operation(summary = "Update an existing recipe")
    public RecipeDto updateRecipe(Authentication authentication, @Valid @RequestBody RecipeDto recipeDto) {
        LOGGER.info("PUT /recipe body: {}", recipeDto);
        try {
            Long groupId = null;
            if (authentication != null) {
                groupId = userService.getGroupIdByUsername(authentication.getName());
            }
            return recipeMapper.recipeToRecipeDto(recipeService.updateRecipe(
                recipeMapper.recipeDtoToRecipe(recipeDto), groupId));
        } catch (UnchangeableException u) {
            LOGGER.error(u.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        } catch (ServiceException s) {
            LOGGER.error(s.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Secured("ROLE_USER")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean deleteRecipe(Authentication authentication, @PathVariable("id") Long id) {
        LOGGER.info("DELETE /delete recipe id: {}", id);
        if (authentication == null) {
            LOGGER.error("You are not logged-in");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not logged-in");
        }
        try {
            recipeService.deleteRecipe(authentication.getName(), id);
            return true;
        } catch (NotFoundException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
        }

    }
}
