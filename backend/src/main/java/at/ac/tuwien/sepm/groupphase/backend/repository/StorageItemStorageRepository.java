package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface StorageItemStorageRepository extends JpaRepository<ItemStorage, Long> {


    @Modifying
    @Query(value = "insert into STORAGE_ITEMS (STORAGE_ID, ITEMS_ID) VALUES (:storageId, :itemId)", nativeQuery = true)
    @Transactional
    void insert(@Param("storageId") Long storageId, @Param("itemId") Long itemId);

    @Modifying
    @Query(value = "delete from STORAGE_ITEMS where (STORAGE_ID = :storageId and ITEMS_ID = :itemId)", nativeQuery = true)
    @Transactional
    int deleteFromTable(@Param("storageId") Long storageId, @Param("itemId") Long itemId);
}
