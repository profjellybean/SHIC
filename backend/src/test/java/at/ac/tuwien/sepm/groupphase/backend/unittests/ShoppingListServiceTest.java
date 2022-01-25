package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.ShoppingListServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class ShoppingListServiceTest implements TestData {
    @Autowired
    private ShoppingListServiceImpl shoppingListService;

    @Test
    public void saveItem() {
        long id = shoppingListService.createNewShoppingList();
        ItemStorage item = new ItemStorage("Test", id);
        shoppingListService.saveItem(item, id, null);
        assertEquals("Test", shoppingListService.findAllByShoppingListId(id).get(0).getName());
    }

    @Test
    public void searchItem() {
        long id = shoppingListService.createNewShoppingList();
        ItemStorage item = new ItemStorage("Test", id);
        shoppingListService.saveItem(item, id, null);
        List<ItemStorage> searchItem = shoppingListService.searchItem(new ItemStorage("Test", id));
        assertEquals(searchItem.get(0).getName(), shoppingListService.findAllByShoppingListId(id).get(0).getName());
    }

}
