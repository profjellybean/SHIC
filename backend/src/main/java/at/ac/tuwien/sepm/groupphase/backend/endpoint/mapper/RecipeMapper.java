package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RecipeDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface RecipeMapper {

    Recipe recipeDtoToRecipe(RecipeDto recipeDto);

    RecipeDto recipeToRecipeDto(Recipe recipe);

    List<RecipeDto> recipeToRecipeDto(List<Recipe> recipe);
}
