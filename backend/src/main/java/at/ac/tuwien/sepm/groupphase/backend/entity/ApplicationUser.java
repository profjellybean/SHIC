package at.ac.tuwien.sepm.groupphase.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "APPLICATION_USER", uniqueConstraints = {@UniqueConstraint(columnNames = {"USERNAME"})})
@NamedQuery(
    name = "findByName",
    query = "SELECT c FROM ApplicationUser c WHERE c.username = :username"
)
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private byte[] image;

    @Column(nullable = false, name = "USERNAME")
    private String username;

    @Column(nullable = false, name = "PASSWORD")
    private String password;

    @Column(nullable = false, name = "EMAIL")
    private String email;

    @Column(nullable = false, name = "CONFIRMATIONTOKEN")
    private Long confirmationToken;

    @OneToOne
    @JsonBackReference
    private UserGroup currGroup;

    private Long privList;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Bill> bills;

    public ApplicationUser() {
    }

    public ApplicationUser(String username, String password, Long shoppingListId, String email) {
        this.username = username;
        this.password = password;
        this.privList = shoppingListId;
        this.email = email;
        this.confirmationToken = 0L;
    }


    public ApplicationUser(String username, String password, Long shoppingListId, String email, Long confirmationToken) {
        this.username = username;
        this.password = password;
        this.privList = shoppingListId;
        this.email = email;
        this.confirmationToken = confirmationToken;
    }

    public ApplicationUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public ApplicationUser(String username, String password, UserGroup currGroup, Long privList) {
        this.username = username;
        this.password = password;
        this.currGroup = currGroup;
        this.privList = privList;
    }

    public ApplicationUser(Long id, String username, String email, Long confirmationToken, UserGroup currGroup, Long privList) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.confirmationToken = confirmationToken;
        this.currGroup = currGroup;
        this.privList = privList;
    }


    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Long getPrivList() {
        return privList;
    }

    public void setPrivList(Long privList) {
        this.privList = privList;
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


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserGroup getCurrGroup() {
        return currGroup;
    }

    public void setCurrGroup(UserGroup currGroup) {
        this.currGroup = currGroup;
    }

    public Long getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(Long confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public Set<Bill> getBills() {
        return bills;
    }

    public void setBills(Set<Bill> bills) {
        this.bills = bills;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApplicationUser that = (ApplicationUser) o;
        return Objects.equals(id, that.id)
            && Objects.equals(username, that.username)
            && Objects.equals(password, that.password)
            && Objects.equals(privList, that.privList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, currGroup, privList);
    }

    @Override
    public String toString() {
        return "ApplicationUser{"
            +
            "id=" + id
            +
            ", username='" + username + '\''
            +
            ", password='" + password + '\''
            +
            ", email='" + email + '\''
            +
            ", confirmationToken=" + confirmationToken
            +
            ", currGroup=" + currGroup
            +
            ", privList=" + privList
            +
            ", bills=" + bills
            +
            '}';
    }
}

