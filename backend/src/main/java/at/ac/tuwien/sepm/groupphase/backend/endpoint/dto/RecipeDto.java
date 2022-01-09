package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.RecipeCategory;

import java.util.Objects;
import java.util.Set;

public class RecipeDto {

    private Long id;

    private String name;

    private String description;

    private Set<ItemStorageDto> ingredients;

    private Set<RecipeCategory> categories;

    private Long groupId;

    public RecipeDto() { }

    public RecipeDto(Long id, String name, String description, Set<ItemStorageDto> ingredients, Set<RecipeCategory> categories, Long groupId) {
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

    public Set<ItemStorageDto> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<ItemStorageDto> ingredients) {
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
        RecipeDto recipeDto = (RecipeDto) o;
        return Objects.equals(id, recipeDto.id)
            && Objects.equals(name, recipeDto.name)
            && Objects.equals(description, recipeDto.description)
            && Objects.equals(ingredients, recipeDto.ingredients)
            && Objects.equals(categories, recipeDto.categories)
            && Objects.equals(groupId, recipeDto.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, ingredients, categories, groupId);
    }

    @Override
    public String toString() {
        return "RecipeDto{"
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
}
