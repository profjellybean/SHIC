package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ItemStorageRepository extends JpaRepository<ItemStorage, Long> {

    List<ItemStorage> findAllByStorageId(Long id);

    List<ItemStorage> findAllByStorageIdOrderByNameAsc(Long id);

    List<ItemStorage> findAllByShoppingListId(Long id);

    Optional<ItemStorage> findByName(String name);

    void deleteItemStorageByStorageId(Long id);

    List<ItemStorage> findAllByStorageIdAndNameContainingIgnoreCase(Long id, String name);

    void deleteById(Long id);

    @Modifying
    @Query(value = "SELECT * FROM ITEM_STORAGE WHERE (STORAGE_ID = :storageId) AND ((:amount = 0) OR (AMOUNT>= :amount)) AND ((:locationTag IS NULL) OR (LOCATION_TAG = :locationTag)) "
                                + "AND ((:name IS NULL) OR (LOWER(NAME) like LOWER(:name))) "
                            + "AND ((:notes IS NULL) OR (LOWER(NOTES) like LOWER(:notes))) AND ((:expDate IS NULL) OR (EXP_DATE >= :expDate))", nativeQuery = true)
    @Transactional
    List<ItemStorage> findAllByItemStorage(@Param("storageId") Long storageId, @Param("amount") int amount, @Param("locationTag") String locationTag, @Param("name") String name,
                                           @Param("notes") String notes, @Param("expDate") Date expDate);

    @Query("SELECT i from ItemStorage i WHERE i.storageId = ?1 AND i.name= ?2 AND i.unitOfQuantity = ?4 AND i.amount >= ?3")
    Optional<ItemStorage> itemExistsInStorage(Long storageId, String itemName, int amount, UnitOfQuantity unitOfQuantity);

    @Transactional
    @Modifying
    @Query("UPDATE ItemStorage i SET i.amount = i.amount - ?2 WHERE i = ?1")
    void reduceQuantity(ItemStorage item, int amount);
}
