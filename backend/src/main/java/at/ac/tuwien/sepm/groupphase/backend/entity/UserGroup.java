package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.Objects;
import java.util.Set;

@Entity
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany
    @Column
    private Set<ApplicationUser> user;
    @Column
    private Long storageId;
    @Column
    private Long publicShoppingListId;

    public UserGroup(Long storageId, Long publicShoppingListId) {
        this.storageId = storageId;
        this.publicShoppingListId = publicShoppingListId;
    }

    public Long getPublicShoppingListId() {
        return publicShoppingListId;
    }

    public void setPublicShoppingListId(Long publicShoppingListId) {
        this.publicShoppingListId = publicShoppingListId;
    }

    public UserGroup() {
    }

    public UserGroup(Long storageId) {
        this.storageId = storageId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<ApplicationUser> getUser() {
        return user;
    }

    public void setUser(Set<ApplicationUser> user) {
        this.user = user;
    }

    public Long getStorageId() {
        return storageId;
    }

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserGroup userGroup = (UserGroup) o;
        return Objects.equals(id, userGroup.id)
            && Objects.equals(user, userGroup.user)
            && Objects.equals(storageId, userGroup.storageId)
            && Objects.equals(publicShoppingListId, userGroup.publicShoppingListId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, storageId, publicShoppingListId);
    }

    @Override
    public String toString() {
        return "UserGroup{"
            +
            "id=" + id
            +
            ", user=" + user
            +
            ", storageId=" + storageId
            +
            ", publicShoppingListId=" + publicShoppingListId
            +
            '}';
    }
}
