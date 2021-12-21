package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.entity.Message;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepm.groupphase.backend.repository.MessageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class RecipeRepositoryTest implements TestData {

    @Autowired
    private RecipeRepository recipeRepository;

    private final Recipe recipe = Recipe.RecipeBuilder.aRecipe()
        .withName(TEST_RECIPE_NAME)
        .withDescription(TEST_RECIPE_DESCRIPTION)
        .withCategories(TEST_RECIPE_CATEGORIES)
        .build();


    @Test
    public void givenNothing_whenSaveRecipe_thenFindListWithOneElement() {
        recipeRepository.saveAndFlush(recipe);

        assertAll(
            () -> assertNotNull(recipeRepository.findAll().size()),
            () -> assertEquals(TEST_RECIPE_NAME, recipeRepository.findAll().get(0).getName())
        );
    }

    @Test
    public void givenNothing_whenSaveRecipe_thenFindRecipeByIdAndCheckVariables() {
        recipeRepository.saveAndFlush(recipe);

        assertAll(
            () -> assertEquals(TEST_RECIPE_NAME, recipeRepository.findRecipeById(recipe.getId()).getName()),
            () -> assertEquals(TEST_RECIPE_DESCRIPTION, recipeRepository.findRecipeById(recipe.getId()).getDescription()),
            () -> assertEquals(TEST_RECIPE_CATEGORIES, recipeRepository.findRecipeById(recipe.getId()).getCategories())
        );
    }

}
