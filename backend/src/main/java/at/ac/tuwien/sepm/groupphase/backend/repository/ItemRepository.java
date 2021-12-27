package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * Find all item entries.
     *
     * @return orderd list of all item entities
     */
    List<Item> findAll();

    @Query(value = "SELECT * FROM ITEM WHERE ((GROUP_ID = :groupId) OR (GROUP_ID IS NULL)) AND (UPPER(NAME) = UPPER(:name))", nativeQuery = true)
    List<Item> findItemsByNameForGroup(@Param("name") String name, @Param("groupId") Long groupId);

    @Query(value = "SELECT * FROM ITEM WHERE (GROUP_ID = :groupId) OR (GROUP_ID IS NULL)", nativeQuery = true)
    List<Item> findAllItemsForGroup(@Param("groupId") Long groupId);

    List<Item> findAllByGroupId(Long groupId);

    @Override
    Item save(Item item);
}
