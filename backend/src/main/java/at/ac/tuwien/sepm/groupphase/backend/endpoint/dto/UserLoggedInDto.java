package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

public class UserLoggedInDto {

    @NotNull
    private Long id;

    @NotNull
    private String username;

    @NotNull
    private Long privList;

    public UserLoggedInDto(Long id, String username, Long shoppingListId) {
        this.id = id;
        this.username = username;
        this.privList = shoppingListId;
    }

    public Long getPrivList() {return privList;}

    public void setPrivList(Long privList) {this.privList = privList;}

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

}
