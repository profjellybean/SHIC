package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Register;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegisterRepository extends JpaRepository<Register, Long> {
    /**
     * Find a register by a given id.
     *
     * @return the requested register
     */
    Optional<Register> findRegisterById(Long id);

    /**
     * Save a given register.
     *
     * @return the saved register
     */
    Register save(Register register);
}
