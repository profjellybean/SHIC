package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RecipeDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.RecipeMapper;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping
    @PermitAll
    @Transactional
    @Operation(summary = "Get list of all recipes") //TODO: add security
    public List<RecipeDto> findAll() {
        LOGGER.info("GET /recipe");
        return recipeMapper.recipeToRecipeDto(recipeService.findAll());
    }

    @GetMapping(value = "/{id}")
    @PermitAll
    @Transactional
    @Operation(summary = "Get recipe by id") //TODO: add security
    public RecipeDto findRecipeById(@PathVariable("id") Long id) {
        LOGGER.info("GET /recipe by id");
        return recipeMapper.recipeToRecipeDto(recipeService.findRecipeById(id));
    }

    @PostMapping
    @PermitAll
    @Transactional
    @Operation(summary = "Add a new Recipe")
    public RecipeDto addRecipe(@RequestBody RecipeDto recipeDto) {
        LOGGER.info("Add recipe {}", recipeDto.getName());
        return recipeMapper.recipeToRecipeDto(recipeService.addRecipe(recipeMapper.recipeDtoToRecipe(recipeDto)));
    }

    @PutMapping(value = "/{id}")
    @PermitAll
    @Operation(summary = "Update an existing recipe") //TODO: add security
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
}
