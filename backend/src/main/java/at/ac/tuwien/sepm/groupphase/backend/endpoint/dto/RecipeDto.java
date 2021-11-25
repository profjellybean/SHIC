package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.RecipeCategory;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

public class RecipeDto {

    private Long id;

    private String name;

    private String description;

    private Set<ItemDto> ingredients;

    private Set<RecipeCategory> categories;

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

    public Set<ItemDto> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<ItemDto> ingredients) {
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeDto recipeDto = (RecipeDto) o;
        return Objects.equals(id, recipeDto.id) && Objects.equals(name, recipeDto.name) && Objects.equals(description, recipeDto.description) && Objects.equals(ingredients, recipeDto.ingredients) && Objects.equals(categories, recipeDto.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, ingredients, categories);
    }

    @Override
    public String toString() {
        return "RecipeDto{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", ingredients=" + ingredients +
            ", categories=" + categories +
            '}';
    }
}
