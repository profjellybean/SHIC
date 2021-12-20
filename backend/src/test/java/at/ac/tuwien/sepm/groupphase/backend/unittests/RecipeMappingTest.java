package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RecipeDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.RecipeMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class RecipeMappingTest implements TestData {

    private final Recipe recipe = Recipe.RecipeBuilder.aRecipe()
        .withName(TEST_RECIPE_NAME)
        .withDescription(TEST_RECIPE_DESCRIPTION)
        .withCategories(TEST_RECIPE_CATEGORIES)
        .build();
    private final RecipeDto recipeDto = new RecipeDto(null, TEST_RECIPE_NAME, TEST_RECIPE_DESCRIPTION,
        null, TEST_RECIPE_CATEGORIES);

    @Autowired
    private RecipeMapper recipeMapper;

    @Test
    public void givenNothing_whenMapRecipeEntityToDto_thenEntityHasAllProperties() {
        RecipeDto recipeDto = recipeMapper.recipeToRecipeDto(recipe);
        assertAll(
            () -> assertEquals(TEST_RECIPE_NAME, recipeDto.getName()),
            () -> assertEquals(TEST_RECIPE_DESCRIPTION, recipeDto.getDescription()),
            () -> assertEquals(TEST_RECIPE_CATEGORIES, recipeDto.getCategories())
        );
    }

    @Test
    public void givenNothing_whenMapListRecipeEntityToDto_thenEntityHasAllProperties() {
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(recipe);
        recipes.add(recipe);

        List<RecipeDto> recipeDtos = recipeMapper.recipeToRecipeDto(recipes);
        assertEquals(2, recipeDtos.size());

        RecipeDto recipeDto = recipeDtos.get(0);
        assertAll(
            () -> assertEquals(TEST_RECIPE_NAME, recipeDto.getName()),
            () -> assertEquals(TEST_RECIPE_DESCRIPTION, recipeDto.getDescription()),
            () -> assertEquals(TEST_RECIPE_CATEGORIES, recipeDto.getCategories())
        );
    }

    @Test
    public void givenNothing_whenMapRecipeDtoToEntity_thenEntityHasAllProperties() {
        Recipe recipe = recipeMapper.recipeDtoToRecipe(recipeDto);

        assertAll(
            () -> assertEquals(TEST_RECIPE_NAME, recipe.getName()),
            () -> assertEquals(TEST_RECIPE_DESCRIPTION, recipe.getDescription()),
            () -> assertEquals(TEST_RECIPE_CATEGORIES, recipe.getCategories())
        );
    }
}
