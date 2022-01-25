package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;


public class UserDto {
    private Long id;
    private String username;
    private UserGroupDto currGroup;
    private Long privList;
    private String email;
    private byte[] image;

    public UserDto(Long id, String username, UserGroupDto currGroup, Long privList, String email, byte[] image) {
        this.id = id;
        this.username = username;
        this.currGroup = currGroup;
        this.privList = privList;
        this.email = email;
        this.image = image;
    }

    public UserDto() {
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public UserGroupDto getCurrGroup() {
        return currGroup;
    }

    public void setCurrGroup(UserGroupDto currGroup) {
        this.currGroup = currGroup;
    }

    public Long getPrivList() {
        return privList;
    }

    public void setPrivList(Long privList) {
        this.privList = privList;
    }

    @Override
    public String toString() {
        return "UserDto{"
            +
            "id=" + id
            +
            ", username='" + username + '\''
            +
            '}';
    }
}
