package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.TrashOrUsed;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TrashOrUsedRepository extends JpaRepository<TrashOrUsed, Long> {
    /**
     * delete a TrashOrUsed before a date.
     */
    void deleteTrashOrUsedByDateLessThan(Date date);

    @Query(value = "SELECT SUM(T.AMOUNT) FROM TRASH_OR_USED AS T WHERE (T.STORAGE_ID = :storageId) AND (YEAR(T.DATE) = YEAR(:date)) AND (MONTH(T.DATE) = MONTH(:date)) AND (DAY(T.DATE) = DAY(:date))AND T.TRASH = :trash", nativeQuery = true)
    Double sumOfSpecificMonth(Long storageId, Date date, boolean trash);

    @Modifying
    @Query(value = "SELECT * FROM TRASH_OR_USED AS T WHERE (T.STORAGE_ID = :storageId) AND (YEAR(T.DATE) = YEAR(:date)) AND (MONTH(T.DATE) = MONTH(:date)) AND T.TRASH = :trash", nativeQuery = true)
    List<TrashOrUsed> getTrashOrUsedByDateEqualsAndStorageIdEqualsAndTrashEquals(Date date, Long storageId, boolean trash);

    @Modifying
    @Query(value = "update TRASH_OR_USED AS T SET AMOUNT = AMOUNT+1 WHERE (T.STORAGE_ID = :storageId) AND (YEAR(T.DATE) = YEAR(:date)) AND (MONTH(T.DATE) = MONTH(:date)) AND (DAY(T.DATE) = DAY(:date))AND"
        + " T.TRASH = :trash", nativeQuery = true)
    void update(Date date, Long storageId, boolean trash);

}
