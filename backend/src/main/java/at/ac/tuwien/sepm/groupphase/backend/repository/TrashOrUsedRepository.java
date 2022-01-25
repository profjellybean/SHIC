package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.TrashOrUsed;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface TrashOrUsedRepository extends JpaRepository<TrashOrUsed, Long> {
    /**
     * delete a TrashOrUsed before a date.
     */
    void deleteTrashOrUsedByDateLessThan(Date date);

    /**
     * count how many items have been thrown away in a specific month.
     * return number of thrown away items.
     */
    @Query(value = "SELECT COUNT(T.ITEM_NAME) from TRASH_OR_USED AS T WHERE (YEAR(T.DATE) = YEAR(:day)) AND (MONTH(T.DATE) = MONTH(:day)) AND (T.STORAGE_ID= :storageId) ORDER BY YEAR(T.DATE), MONTH (T.DATE) DESC ", nativeQuery = true)
    Double sumOfArticlesOfSpecificMonth(@Param("storageId") Long storageId, @Param("day") LocalDate day);

    /**
     * count how many items have been thrown away in a specific year.
     * return number of thrown away items.
     */
    @Query(value = "SELECT COUNT(T.ITEM_NAME) from TRASH_OR_USED AS T WHERE (YEAR(T.DATE) = YEAR(:day)) AND (T.STORAGE_ID= :storageId) GROUP BY YEAR(T.DATE) ORDER BY YEAR(T.DATE) DESC ", nativeQuery = true)
    Double sumOfArticlesOfSpecificYear(@Param("storageId") Long storageId, @Param("day") LocalDate day);

    /**
     * find Trash Or used with specific name and storageId.
     * return list of Trash or used items.
     */
    List<TrashOrUsed> findAllByItemNameEqualsAndStorageIdEquals(@Param("itemName") String itemName, @Param("storageId") Long storageId);


}
