package at.ac.tuwien.sepm.groupphase.backend.unittests;


import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ItemService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class ItemServiceTest {

    @Autowired
    ItemService itemService;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    PlatformTransactionManager txm;

    TransactionStatus txstatus;

    @BeforeEach
    public void setupDBTransaction() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        txstatus = txm.getTransaction(def);
        assumeTrue(txstatus.isNewTransaction());
        txstatus.setRollbackOnly();
    }

    @AfterEach
    public void tearDownDBData() {
        txm.rollback(txstatus);
    }

    @Test
    public void addingCustomItemsAndFindThem_shouldNotThrowException() throws Exception {
        Long fakeGroupId = -11L;
        Item item1 = new Item(null, "TEST_ITEM_1", null, fakeGroupId);
        Item item2 = new Item(null, "TEST_ITEM_2", null, fakeGroupId);

        int sizeBeforePost = itemRepository.findAllItemsForGroup(fakeGroupId).size();
        itemRepository.saveAndFlush(item1);
        itemRepository.saveAndFlush(item2);
        int sizeAfterPost = itemRepository.findAllItemsForGroup(fakeGroupId).size();

        assertEquals(sizeBeforePost + 2, sizeAfterPost);
    }

    @Test
    public void checkingForBluePrintOfNewItem_shouldAddItemToItemTable() throws Exception {
        Long fakeGroupId = -12L;
        ItemStorage item1 = new ItemStorage();
        item1.setName(String.valueOf(item1.hashCode()));

        int sizeBeforePost = itemRepository.findAllItemsForGroup(fakeGroupId).size();
        itemService.checkForBluePrintForGroup(item1, fakeGroupId);
        int sizeAfterPost = itemRepository.findAllItemsForGroup(fakeGroupId).size();

        assertEquals(sizeBeforePost + 1, sizeAfterPost);
    }

    @Test
    public void givenNoGroupId_whenCheckingForBluePrintOfNewItem_shouldNotAddItemToItemTable() throws Exception {
        ItemStorage item1 = new ItemStorage();
        item1.setName("TEST_NAME");

        int sizeBeforePost = itemRepository.findAll().size();
        itemService.checkForBluePrintForGroup(item1, null);
        int sizeAfterPost = itemRepository.findAll().size();

        assertEquals(sizeBeforePost, sizeAfterPost);
    }

    @Test
    public void givenNoItem_whenCheckingForBluePrintOfNewItem_shouldThrowValidationException() throws Exception {

        assertThrows(ValidationException.class, () -> itemService.checkForBluePrintForGroup(null, -1L));
    }

}
