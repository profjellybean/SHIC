package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface StorageRepository extends JpaRepository<ItemStorage, Long> {
    List<ItemStorage> findAllByNameLike(String name);

    @Modifying
    @Query(value = "insert into STORAGE_ITEMS (storage_id, ITEMS_ID) VALUES (:id, :idItem)", nativeQuery = true)
    @Transactional
    void insert(@Param("idItem") Long idItem, @Param("id") Long id);


    List<ItemStorage> findAllByStorageId(Long id);
}
