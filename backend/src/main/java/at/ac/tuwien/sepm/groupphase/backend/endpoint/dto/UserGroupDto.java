package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.util.Set;

public class UserGroupDto {
    private Long id;
    private Set<String> user;
    private String name;
    private Long storageId;
    private Long publicShoppingListId;
    private Long registerId;

    public UserGroupDto() {
    }

    public UserGroupDto(Long id, Set<String> user, Long storageId, Long publicShoppingListId, Long registerId, String name) {
        this.id = id;
        this.user = user;
        this.storageId = storageId;
        this.publicShoppingListId = publicShoppingListId;
        this.registerId = registerId;
        this.name = name;
    }

    public Long getRegisterId() {
        return registerId;
    }

    public void setRegisterId(Long registerId) {
        this.registerId = registerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "UserGroupDto{"
            +
            "id=" + id
            +
            ", name='" + name + '\''
            +
            '}';
    }
}
