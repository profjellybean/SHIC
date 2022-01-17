package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.LocationClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<LocationClass, Long> {

    List<LocationClass> findAllByStorageId(Long storageId);

    List<LocationClass> findAllByName(String name);

    List<LocationClass> findAllByNameAndStorageId(String name, Long storageId);

    void deleteById(Long id);
}
