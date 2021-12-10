package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemStorageRepository extends JpaRepository<ItemStorage, Long> {
    List<ItemStorage> findAllByStorageId(Long id);

    List<ItemStorage> findAllByStorageIdAndNameContainingIgnoreCase(Long id, String name);

    void deleteById(Long id);
}
