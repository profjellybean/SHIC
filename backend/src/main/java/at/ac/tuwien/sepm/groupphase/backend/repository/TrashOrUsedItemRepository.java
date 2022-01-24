package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.TrashOrUsedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrashOrUsedItemRepository extends JpaRepository<TrashOrUsedItem, Long> {

    List<TrashOrUsedItem> findAllByItemNameEqualsAndStorageIdEquals(String name, Long storageId);

    @Query(value = "SELECT * from TRASH_OR_USED_ITEM WHERE STORAGE_ID = :storageId ORDER BY AMOUNT DESC LIMIT 10", nativeQuery = true)
    List<TrashOrUsedItem> getTenMostOftenThrownAwayItem(@Param("storageId") Long storageId);

}
