package at.ac.tuwien.sepm.groupphase.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @OneToMany
    @Column
    @JsonManagedReference
    private Set<ApplicationUser> user;
    @Column
    private Long storageId;
    @Column
    private Long publicShoppingListId;
    @Column
    private Long registerId;

    public UserGroup() {
    }

    public UserGroup(Long storageId) {
        this.storageId = storageId;
    }

    public UserGroup(Long storageId, Long publicShoppingListId) {
        this.storageId = storageId;
        this.publicShoppingListId = publicShoppingListId;
    }

    public UserGroup(Long publicStorageId, Long publicShoppingListId, Long registerId, HashSet<ApplicationUser> applicationUsers, String name) {
        this.publicShoppingListId = publicShoppingListId;
        this.storageId = publicStorageId;
        this.registerId = registerId;
        this.user = applicationUsers;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPublicShoppingListId() {
        return publicShoppingListId;
    }

    public void setPublicShoppingListId(Long publicShoppingListId) {
        this.publicShoppingListId = publicShoppingListId;
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

    public Long getRegisterId() {
        return registerId;
    }

    public void setRegisterId(Long registerId) {
        this.registerId = registerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserGroup group = (UserGroup) o;
        return Objects.equals(id, group.id) && Objects.equals(user, group.user) && Objects.equals(storageId, group.storageId)
            && Objects.equals(publicShoppingListId, group.publicShoppingListId) && Objects.equals(registerId, group.registerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, storageId, publicShoppingListId, registerId);
    }

    @Override
    public String toString() {
        return "UserGroup{"
            + "id=" + id
            + ", user=" + user
            + ", storageId=" + storageId
            + ", publicShoppingListId=" + publicShoppingListId
            + ", registerId=" + registerId
            + '}';
    }
}
