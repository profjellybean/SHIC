package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemStorageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ItemStorageMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.Location;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class ItemStorageMappingTest implements TestData {

    private final ItemStorage itemStorage = new ItemStorage(TEST_ITEMSTORAGE_NAME, TEST_ITEMSTORAGE_NOTES, null, null, TEST_ITEMSTORAGE_AMOUNT, Location.fridge.toString(), TEST_ITEMSTORAGE_UNITOFQUANTITY, TEST_ITEMSTORAGE_STORAGEID, TEST_ITEMSTORAGE_SHOPPINGLISTID);
    private final ItemStorageDto itemStorageDto = new ItemStorageDto(TEST_ITEMSTORAGE_STORAGEID, TEST_ITEMSTORAGE_SHOPPINGLISTID, null, TEST_ITEMSTORAGE_NAME, TEST_ITEMSTORAGE_UNITOFQUANTITYDTO, TEST_ITEMSTORAGE_NOTES, null, null, TEST_ITEMSTORAGE_AMOUNT, Location.fridge.toString());

    @Autowired
    ItemStorageMapper itemStorageMapper;


    @Test
    public void givenNothing_whenMapItemStorageEntityToDto_thenDtoHasAllProperties() {
        ItemStorageDto itemStorageDto = itemStorageMapper.itemStorageToItemStorageDto(itemStorage);
        assertAll(
            () -> assertEquals(TEST_ITEMSTORAGE_NAME, itemStorageDto.getName()),
            () -> assertEquals(TEST_ITEMSTORAGE_NOTES, itemStorageDto.getNotes()),
            () -> assertEquals(TEST_ITEMSTORAGE_UNITOFQUANTITY.getName(), itemStorageDto.getQuantity().getName()),
            () -> assertEquals(TEST_ITEMSTORAGE_STORAGEID, itemStorageDto.getStorageId()),
            () -> assertEquals(TEST_ITEMSTORAGE_SHOPPINGLISTID, itemStorageDto.getShoppingListId()),
            () -> assertEquals(TEST_ITEMSTORAGE_AMOUNT, itemStorageDto.getAmount())
        );
    }

    @Test
    public void givenNothing_whenMapItemStorageDtoToEntity_thenEntityHasAllProperties() {
        ItemStorage itemStorage = itemStorageMapper.itemStorageDtoToItemStorage(itemStorageDto);
        assertAll(
            () -> assertEquals(TEST_ITEMSTORAGE_NAME, itemStorage.getName()),
            () -> assertEquals(TEST_ITEMSTORAGE_NOTES, itemStorage.getNotes()),
            () -> assertEquals(TEST_ITEMSTORAGE_UNITOFQUANTITY.getName(), itemStorage.getQuantity().getName()),
            () -> assertEquals(TEST_ITEMSTORAGE_STORAGEID, itemStorage.getStorageId()),
            () -> assertEquals(TEST_ITEMSTORAGE_SHOPPINGLISTID, itemStorage.getShoppingListId()),
            () -> assertEquals(TEST_ITEMSTORAGE_AMOUNT, itemStorage.getAmount())
        );
    }


}
