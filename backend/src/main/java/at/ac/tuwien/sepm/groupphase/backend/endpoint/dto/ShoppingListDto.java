package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;

import java.util.Objects;
import java.util.Set;

public class ShoppingListDto {

    private Long id;

    private String name;

    private String notes;

    private Set<ItemStorageDto> items;

    // TODO: change to UserDto
    private ApplicationUser owner;

    public ShoppingListDto() { }

    public ShoppingListDto(Long id, String name, String notes, Set<ItemStorageDto> items, ApplicationUser owner) {
        this.id = id;
        this.name = name;
        this.notes = notes;
        this.items = items;
        this.owner = owner;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Set<ItemStorageDto> getItems() {
        return items;
    }

    public void setItems(Set<ItemStorageDto> items) {
        this.items = items;
    }

    public ApplicationUser getOwner() {
        return owner;
    }

    public void setOwner(ApplicationUser owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingListDto that = (ShoppingListDto) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(notes, that.notes) && Objects.equals(items, that.items) && Objects.equals(owner, that.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, notes, items, owner);
    }

    @Override
    public String toString() {
        return "ShoppingListDto{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", notes='" + notes + '\'' +
            ", items=" + items +
            ", owner=" + owner +
            '}';
    }
}
