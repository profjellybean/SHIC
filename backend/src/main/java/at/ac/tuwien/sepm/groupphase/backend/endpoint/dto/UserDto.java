package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.UserGroup;

public class UserDto {
    private Long id;
    private String username;
    private UserGroup currGroup;
    private Long privList;

    public UserDto(Long id, String username, UserGroup currGroup, Long privList) {
        this.id = id;
        this.username = username;
        this.currGroup = currGroup;
        this.privList = privList;
    }

    public UserDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserGroup getCurrGroup() {
        return currGroup;
    }

    public void setCurrGroup(UserGroup currGroup) {
        this.currGroup = currGroup;
    }

    public Long getPrivList() {
        return privList;
    }

    public void setPrivList(Long privList) {
        this.privList = privList;
    }
}
