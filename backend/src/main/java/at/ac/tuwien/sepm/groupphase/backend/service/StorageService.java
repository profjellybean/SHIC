package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.entity.Storage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface StorageService {
    /**
     * Delete an item in the context of Spring Security based on the id
     *
     * @param id the id
     * @return a Spring Security user
     */

    Item deleteItemById(Long id);

    Item saveItem(Item item);

    List<Item> getAll();
}
