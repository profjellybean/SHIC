package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * Find all item entries.
     *
     * @return orderd list of all item entities
     */
    List<Item> findAll();

    /**
     * Find all item entries, sorted by Name A-Z.
     *
     * @return orderd list of all item entities
     */
    List<Item> findAllByOrderByNameAsc();

    @Query(value = "SELECT * FROM ITEM WHERE ((GROUP_ID = :groupId) OR (GROUP_ID IS NULL)) AND (UPPER(NAME) = UPPER(:name))", nativeQuery = true)
    List<Item> findItemsByNameForGroup(@Param("name") String name, @Param("groupId") Long groupId);

    @Query(value = "SELECT * FROM ITEM WHERE (GROUP_ID = :groupId) OR (GROUP_ID IS NULL)", nativeQuery = true)
    List<Item> findAllItemsForGroupOrderByNameAsc(@Param("groupId") Long groupId);

    @Query(value = "SELECT * FROM ITEM WHERE (GROUP_ID = :groupId) OR (GROUP_ID IS NULL)", nativeQuery = true)
    List<Item> findAllItemsForGroup(@Param("groupId") Long groupId);

    List<Item> findAllByGroupIdOrderByNameAsc(Long groupId);


    @Query(value = "SELECT * FROM ITEM WHERE ((GROUP_ID = :groupId) OR (GROUP_ID IS NULL)) AND ((:name IS NULL) OR (LOWER(NAME) like LOWER(:name)))", nativeQuery = true)
    List<Item> findAllItemsByNameForGroup(@Param("name") String name, @Param("groupId") Long groupId);

    @Override
    Item save(Item item);
}
