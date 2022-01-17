package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PendingEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "EMAIL")
    private String email;

    @Column(nullable = false, name = "USERNAME")
    private String username;

    @Column(nullable = false, name = "CONFIRMATIONTOKEN")
    private Long confirmationToken;


    public PendingEmail(String email, Long confirmationToken, String username) {
        this.email = email;
        this.confirmationToken = confirmationToken;
        this.username = username;
    }

    public PendingEmail() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public Long getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(Long confirmationToken) {
        this.confirmationToken = confirmationToken;
    }
}
