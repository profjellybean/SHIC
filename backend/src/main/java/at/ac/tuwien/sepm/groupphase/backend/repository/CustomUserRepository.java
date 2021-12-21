package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomUserRepository extends JpaRepository<ApplicationUser, Long> {

    Optional<ApplicationUser> findUserByUsername(String username);

    @Override
    <S extends ApplicationUser> S save(S entity);


    @Query("SELECT u.currGroup.publicShoppingListId FROM ApplicationUser u WHERE u.username = ?1")
    Long getPublicShoppingListIdByName(String username);

    @Query("SELECT s FROM ShoppingList s WHERE s.id = (SELECT u.currGroup.publicShoppingListId FROM ApplicationUser u WHERE u.username = ?1)")
    ShoppingList getPublicShoppingListByName(String username);

    @Query ("SELECT s from ShoppingList s WHERE s.id = (SELECT u.privList FROM ApplicationUser u WHERE u.username = ?1)")
    ShoppingList getPrivateShoppingListByName(String username);

    @Query ("SELECT s.items from ShoppingList s WHERE s.id = (SELECT u.privList FROM ApplicationUser u WHERE u.username = ?1) OR s.id = (SELECT u.currGroup.publicShoppingListId FROM ApplicationUser u WHERE u.username = ?1) ")
    List<ItemStorage> getAvailableItems(String username);

    @Query ("SELECT u.currGroup.storageId FROM ApplicationUser u WHERE u.username = ?1")
    Long loadGroupStorageByUsername(String username);
}
