package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Storage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany
    private Set<Item> items = new HashSet<>();

    public Storage(Set<Item> items) {
        this.items = items;
    }

    public Storage() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }
}
