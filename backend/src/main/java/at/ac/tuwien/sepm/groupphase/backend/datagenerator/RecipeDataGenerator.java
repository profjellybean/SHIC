package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.RecipeCategory;
import at.ac.tuwien.sepm.groupphase.backend.repository.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Profile("generateData")
@Component
public class RecipeDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_RECIPES_TO_GENERATE = 5;
    private static final String TEST_RECIPE_NAME = "Name";
    private static final String TEST_RECIPE_DESCRIPTION = "Description of Recipe";
    private static final Set<ItemStorage> TEST_RECIPE_INGREDIENTS = new HashSet<>(Arrays.asList(
        new ItemStorage( "RecipeTestIngredient1", "notes", null, null, 10, null, null,1L),
        new ItemStorage("RecipeTestIngredient2", "notes", null, null, 20, null, null,1L),
        new ItemStorage("RecipeTestIngredient3", "notes", null, null, 30, null, null,1L)));
    private static final Set<RecipeCategory> TEST_RECIPE_CATEGORIES = new HashSet<>(Arrays.asList(RecipeCategory.breakfast, RecipeCategory.vegetarian));

    private final RecipeRepository recipeRepository;

    public RecipeDataGenerator(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @PostConstruct
    private void generateRecipes() {
        if(recipeRepository.findAll().size() > 0) {
            LOGGER.debug("recipes already generated");
        } else {
            LOGGER.debug("generating {} recipes", NUMBER_OF_RECIPES_TO_GENERATE);

            for (int i = 0; i < NUMBER_OF_RECIPES_TO_GENERATE; i++) {
                Recipe recipe = Recipe.RecipeBuilder.aRecipe()
                    .withName(TEST_RECIPE_NAME+" "+i)
                    .withDescription(TEST_RECIPE_DESCRIPTION+" "+i)
                    //.withIngredients(TEST_RECIPE_INGREDIENTS)
                    .withCategories(TEST_RECIPE_CATEGORIES)
                    .build();
                LOGGER.debug("saving recipe {}", recipe);
                recipeRepository.save(recipe);
            }
        }
    }
}
