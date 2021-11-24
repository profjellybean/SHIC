package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

//TODO: replace this class with a correct ApplicationUser JPARepository implementation
@Repository
public class UserRepository {

    private final ApplicationUser user;

    @Autowired
    public UserRepository(PasswordEncoder passwordEncoder) {
        user = new ApplicationUser("username", passwordEncoder.encode("password"));
    }

    public ApplicationUser findUserByUsername(String username) {
        if (username.equals(user.getUsername())) {
            return user;
        }
        return null; // In this case null is returned to fake Repository behavior
    }
}
