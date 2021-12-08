package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * Find all item entries
     *
     * @return orderd list of all item entities
     */
    List<Item> findAll();
}
