package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.StorageServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class StorageServiceTest implements TestData {
    @Autowired
    private StorageServiceImpl storageService;

    @Test
    public void gettingStorageThatDoesntExistShouldThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> storageService.findStorageById(-100L));
    }

   //@Test
    public void saveItem() {
        long id = storageService.createNewStorage();
        ItemStorage item = new ItemStorage(id, "Test");
        storageService.saveItem(item, null);
        assertEquals("Test", storageService.getAll(id).get(0).getName());
    }

    //@Test
    public void deleteItem() {
        long id = storageService.createNewStorage();
        ItemStorage item = new ItemStorage(id, "Test1");
        item = storageService.saveItem(item, null);
        assertEquals("Test1", storageService.getAll(id).get(0).getName());
        storageService.deleteItemById(item.getId());
        assertTrue(storageService.searchItemName(id, "Test1").isEmpty());
    }
}
