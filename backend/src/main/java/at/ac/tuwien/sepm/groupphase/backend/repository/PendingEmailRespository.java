package at.ac.tuwien.sepm.groupphase.backend.repository;


import at.ac.tuwien.sepm.groupphase.backend.entity.PendingEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface PendingEmailRespository extends JpaRepository<PendingEmail, Long> {


    @Override
    <S extends PendingEmail> S save(S entity);

    @Query("SELECT p FROM PendingEmail p WHERE p.confirmationToken = ?1 AND p.username = ?2")
    Optional<PendingEmail> findEmailByConfirmationToken(Long confirmationToken, String username);

    @Transactional
    @Modifying
    @Query("DELETE FROM PendingEmail p WHERE p.email = ?1")
    void deleteByEmail(String email);
}
