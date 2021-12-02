package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Message;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    /**
     * Find all recipes entries
     *
     * @return orderd list of all recipes entities
     */
    List<Recipe>findAll();

    /**
     * Find one recipe entry by id
     *
     * @return recipe with given id
     */
    Recipe findRecipeById(Long id);
}
