package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository {

    Optional<ApplicationUser> findUserByUsername(String username);

    void createUser(ApplicationUser newUser);

    Optional<ApplicationUser> findById(Long id);
}
