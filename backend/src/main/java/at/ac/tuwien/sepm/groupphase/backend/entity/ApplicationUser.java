package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;

@Entity
@Table(name="APPLICATION_USER")
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


    public ApplicationUser() {
    }

    public ApplicationUser(String username, String password) {
        this.username = username;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
