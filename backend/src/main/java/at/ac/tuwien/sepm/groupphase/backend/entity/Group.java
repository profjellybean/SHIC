package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @Column
    private Set<ApplicationUser> user;

    @Column
    private Long storageId;
    @Column
    private Long recipeId = storageId;
    @Column
    private Long shoppingListId = storageId;
    //TODO: Kassa

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
