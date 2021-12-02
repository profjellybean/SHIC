package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomUserRepository extends JpaRepository<ApplicationUser, Long> {

    Optional<ApplicationUser> findUserByUsername(String username);

    @Override
    <S extends ApplicationUser> S save(S entity);



}
