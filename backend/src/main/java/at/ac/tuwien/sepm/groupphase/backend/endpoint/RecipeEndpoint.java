package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BillDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RecipeDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.RecipeMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepm.groupphase.backend.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import javax.transaction.Transactional;
import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/recipe")
public class RecipeEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final RecipeMapper recipeMapper;
    private final RecipeService recipeService;

    @Autowired
    public RecipeEndpoint(RecipeMapper recipeMapper, RecipeService recipeService) {
        this.recipeMapper = recipeMapper;
        this.recipeService = recipeService;
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
}
