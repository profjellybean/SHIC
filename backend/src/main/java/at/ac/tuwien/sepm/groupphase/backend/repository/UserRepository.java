package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface UserRepository {

    Optional<ApplicationUser> findUserByUsername(String username);

    void createUser(ApplicationUser newUser);
}
