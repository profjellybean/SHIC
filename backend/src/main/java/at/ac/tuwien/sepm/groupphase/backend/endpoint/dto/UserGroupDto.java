package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;

import java.util.Set;

public class UserGroupDto {
    private Long id;
    private Set<String> user;
    private Long storageId;
    private Long publicShoppingListId;

    public UserGroupDto() {
    }

    public UserGroupDto(Long id, Set<String> user, Long storageId, Long publicShoppingListId) {
        this.id = id;
        this.user = user;
        this.storageId = storageId;
        this.publicShoppingListId = publicShoppingListId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<String> getUser() {
        return user;
    }

    public void setUser(Set<String> user) {
        this.user = user;
    }

    public Long getStorageId() {
        return storageId;
    }

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
    }

    public Long getPublicShoppingListId() {
        return publicShoppingListId;
    }

    public void setPublicShoppingListId(Long publicShoppingListId) {
        this.publicShoppingListId = publicShoppingListId;
    }
}
