package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    /**
     * Find all recipes entries.
     *
     * @return list of all recipes entities
     */
    List<Recipe> findAll();

    /**
     * Find all recipes where name contains parameter name.
     *
     * @param name String that should be in the recipes name
     *
     * @return list of all recipes with names that contain specified String
     */
    List<Recipe> findAllByNameContainingIgnoreCaseOrderByNameAsc(String name);

    /**
     * Find all recipes entries, sorted by name A-Z.
     *
     * @return sorted list of all recipes entities
     */
    List<Recipe> findAllByOrderByNameAsc();

    /**
     * Find one recipe entry by id.
     *
     * @return recipe with given id
     */
    Recipe findRecipeById(Long id);

    /**
     * find one recipe by its name.
     *
     * @param name of the recipe
     *
     * @return recipe with given name
     */
    Recipe findByName(String name);

    /**
     * delete recipe by id.
     *
     * @param id of the recipe
     *
     * @return recipe with given id
     */
    Recipe deleteRecipeById(Long id);
}
