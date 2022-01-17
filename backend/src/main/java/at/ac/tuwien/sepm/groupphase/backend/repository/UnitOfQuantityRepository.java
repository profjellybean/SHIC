package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UnitOfQuantityRepository extends JpaRepository<UnitOfQuantity, Long> {
    Optional<UnitOfQuantity> findByName(String name);

    UnitOfQuantity getUnitOfQuantityById(Long id);

    List<UnitOfQuantity> findAll();

    List<UnitOfQuantity> findAllByGroupIdOrGroupIdIsNull(Long groupId);

    @Override
    void deleteAll();
}
