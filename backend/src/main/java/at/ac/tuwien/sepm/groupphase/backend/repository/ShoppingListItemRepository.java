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
public interface ShoppingListItemRepository extends JpaRepository<ItemStorage, Long> {

    List<ItemStorage> findAllByStorageId(Long storageId);

    List<ItemStorage> findAllByShoppingListId(Long shoppingListId);

    @Modifying
    @Query(value = "insert into SHOPPING_LIST_ITEMS (SHOPPING_LIST_ID, ITEMS_ID) VALUES (:shoppingListId, :itemId)", nativeQuery = true)
    @Transactional
    void insert(@Param("shoppingListId") Long shoppingListId, @Param("itemId") Long itemId);

    @Modifying
    @Query(value = "delete from SHOPPING_LIST_ITEMS where SHOPPING_LIST_ID = :shoppingListId and ITEMS_ID = :itemId", nativeQuery = true)
    @Transactional
    void deleteFromTable(@Param("shoppingListId") Long shoppingListId, @Param("itemId") Long itemId);
}
