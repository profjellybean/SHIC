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
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "description")
    private String description;

    @OneToMany(cascade = CascadeType.MERGE)
    @Column(nullable = true, name = "ingredients")
    private Set<ItemStorage> ingredients;

    @Column(name = "groupId")
    private Long groupId;

    @Column(name = "categories")
    @ElementCollection(targetClass = RecipeCategory.class)
    @CollectionTable
    @Enumerated(EnumType.STRING)
    private Set<RecipeCategory> categories;

    public Recipe() {
    }

    public Recipe(Long id, String name, String description, Set<ItemStorage> ingredients, Set<RecipeCategory> categories, Long groupId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.categories = categories;
        this.groupId = groupId;
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

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
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
        return Objects.equals(id, recipe.id)
            && Objects.equals(name, recipe.name)
            && Objects.equals(description, recipe.description)
            && Objects.equals(ingredients, recipe.ingredients)
            && Objects.equals(categories, recipe.categories)
            && Objects.equals(groupId, recipe.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, ingredients, categories, groupId);
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
            ", groupId=" + groupId
            +
            '}';
    }

    public static final class RecipeBuilder {
        private Long id;
        private String name;
        private String description;
        private Set<ItemStorage> ingredients;
        private Set<RecipeCategory> categories;
        private Long groupId;

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

        public RecipeBuilder withGroupId(Long groupId) {
            this.groupId = groupId;
            return this;
        }

        public Recipe build() {
            Recipe recipe = new Recipe();
            recipe.setId(this.id);
            recipe.setName(this.name);
            recipe.setDescription(this.description);
            recipe.setIngredients(this.ingredients);
            recipe.setCategories(this.categories);
            recipe.setGroupId(this.groupId);
            return recipe;
        }

    }

}
