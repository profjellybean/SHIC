package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.RecipeCategory;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Objects;
import java.util.Set;

@Entity
//@Transactional
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "description")
    private String description;

    @OneToMany(cascade = CascadeType.MERGE)//(fetch=FetchType.EAGER)
    @Column(nullable = true, name = "ingredients")
    private Set<ItemStorage> ingredients;

    //@OneToMany
    @Column(name = "categories")
    @ElementCollection(targetClass = RecipeCategory.class)
    @CollectionTable
    @Enumerated(EnumType.STRING)
    private Set<RecipeCategory> categories;

    public Recipe() { }

    public Recipe(Long id, String name, String description, Set<ItemStorage> ingredients, Set<RecipeCategory> categories) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.categories = categories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ItemStorage> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<ItemStorage> ingredients) {
        this.ingredients = ingredients;
    }

    public Set<RecipeCategory> getCategories() {
        return categories;
    }

    public void setCategories(Set<RecipeCategory> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Recipe recipe = (Recipe) o;
        return Objects.equals(id, recipe.id) && Objects.equals(name, recipe.name) && Objects.equals(description, recipe.description) && Objects.equals(ingredients, recipe.ingredients) && Objects.equals(categories, recipe.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, ingredients, categories);
    }

    @Override
    public String toString() {
        return "Recipe{"
            +
            "id=" + id
            +
            ", name='" + name + '\''
            +
            ", description='" + description + '\''
            +
            ", ingredients=" + ingredients
            +
            ", categories=" + categories
            +
            '}';
    }

    public static final class RecipeBuilder {
        private Long id;
        private String name;
        private String description;
        private Set<ItemStorage> ingredients;
        private Set<RecipeCategory> categories;

        private RecipeBuilder() {
        }

        public static RecipeBuilder aRecipe() {
            return new RecipeBuilder();
        }

        public RecipeBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public RecipeBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public RecipeBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public RecipeBuilder withIngredients(Set<ItemStorage> ingredients) {
            this.ingredients = ingredients;
            return this;
        }

        public RecipeBuilder withCategories(Set<RecipeCategory> categories) {
            this.categories = categories;
            return this;
        }

        public Recipe build() {
            Recipe recipe = new Recipe();
            recipe.setId(this.id);
            recipe.setName(this.name);
            recipe.setDescription(this.description);
            recipe.setIngredients(this.ingredients);
            recipe.setCategories(this.categories);
            return recipe;
        }

    }

}
