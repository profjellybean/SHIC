package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;

@Entity
@Table(name="APPLICATION_USER",uniqueConstraints={@UniqueConstraint(columnNames={"USERNAME"})})
@NamedQuery(
    name="findByName",
    query= "SELECT c FROM ApplicationUser c WHERE c.username = :username"
)
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, name = "USERNAME")
    private String username;

    @Column(nullable = false, name = "PASSWORD")
    private String password;

    @OneToOne
    private Group currGroup;

    @Column(nullable = false, name = "PRIVLIST")
    private Long privList;

    public ApplicationUser() {
    }

    public ApplicationUser(String username, String password, Long shoppingListId) {
        this.username = username;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Group getCurrGroup() {
        return currGroup;
    }

    public void setCurrGroup(Group currGroup) {
        this.currGroup = currGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationUser that = (ApplicationUser) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password);
    }

}

