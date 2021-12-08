package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
public class ShoppingList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 10000)
    private String notes;


    @OneToMany
    private Set<ItemStorage> items;

    /**
     * if this owner is null, the ShoppingList is public (= for all Users in the Group)
     */
    @OneToOne
    //@Column(nullable = true, name = "owner")
    private ApplicationUser owner;

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

    public Set<ItemStorage> getItems() {
        return items;
    }

    public void setItems(Set<ItemStorage> items) {
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
        ShoppingList that = (ShoppingList) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(notes, that.notes) && Objects.equals(items, that.items) && Objects.equals(owner, that.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, notes, items, owner);
    }

    @Override
    public String toString() {
        return "ShoppingList{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", notes='" + notes + '\'' +
            ", items=" + items +
            ", owner=" + owner +
            '}';
    }

    public static final class ShoppingListBuilder {

        private Long id;
        private String name;
        private String notes;
        private Set<ItemStorage> items;
        private ApplicationUser owner;

        private ShoppingListBuilder() {}

        public static ShoppingListBuilder aShoppingList() { return new ShoppingListBuilder(); }

        public ShoppingListBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public ShoppingListBuilder withName(String name) {
            this.name = name;
            return this;
        }
        public ShoppingListBuilder withNotes(String notes) {
            this.notes = notes;
            return this;
        }

        public ShoppingListBuilder withItems(Set<ItemStorage> items) {
            this.items = items;
            return this;
        }

        public ShoppingListBuilder withOwner(ApplicationUser owner) {
            this.owner = owner;
            return this;
        }

        public ShoppingList build() {
            ShoppingList shoppingList = new ShoppingList();
            shoppingList.setId(this.id);
            shoppingList.setName(this.name);
            shoppingList.setNotes(this.notes);
            shoppingList.setOwner(this.owner);
            shoppingList.setItems(this.items);
            return shoppingList;
        }

    }
}
