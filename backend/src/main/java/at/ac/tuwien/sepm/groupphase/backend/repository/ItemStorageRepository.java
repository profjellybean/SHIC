package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface ItemStorageRepository extends JpaRepository<ItemStorage, Long> {
    List<ItemStorage> findAllByStorageId(Long id);

    List<ItemStorage> findAllByStorageIdAndNameContainingIgnoreCase(Long id, String name);

    void deleteById(Long id);

    List<ItemStorage> findAllByStorageIdAndAmountGreaterThanEqual(Long id, int amount);

    List<ItemStorage> findAllByStorageIdAndExpDateAfterOrExpDateEquals(Long storageId, Date expDate, Date expDate2);

    List<ItemStorage> findAllByStorageIdAndNotesContainingIgnoreCase(Long storageId, String notes);

    List<ItemStorage> findAllByStorageIdAndLocationTagEquals(Long storageId, Location locationTag);

    List<ItemStorage> findAllByStorageIdAndQuantityEquals(Long storageId, Long quantity);

    List<ItemStorage> findAllByStorageIdAndShoppingListIdEquals(Long storageId, Long shoppingListId);

    List<ItemStorage> findAllByStorageIdAndAmountGreaterThanEqualAndLocationTagEqualsAndNameContainingIgnoreCaseAndNotesContainingIgnoreCaseAndExpDateAfterAndExpDateIsNull(
        Long storageId, int amount, Location locationTag, String name, String notes, Date expDate);

    @Modifying
    @Query(value = "SELECT * FROM ITEM_STORAGE WHERE (STORAGE_ID = :storageId) AND ((:amount = 0) OR (AMOUNT>= :amount)) AND ((:locationTag IS NULL) OR (LOCATION_TAG = :locationTag)) "
                                + "AND ((:name IS NULL) OR (LOWER(NAME) like LOWER(:name))) "
                            + "AND ((:notes IS NULL) OR (LOWER(NOTES) like LOWER(:notes))) AND ((:expDate IS NULL) OR (EXP_DATE >= :expDate))", nativeQuery = true)
    @Transactional
    List<ItemStorage> findAllByItemStorage(@Param("storageId") Long storageId, @Param("amount") int amount, @Param("locationTag") String locationTag, @Param("name") String name,
                                           @Param("notes") String notes, @Param("expDate") Date expDate);


}
