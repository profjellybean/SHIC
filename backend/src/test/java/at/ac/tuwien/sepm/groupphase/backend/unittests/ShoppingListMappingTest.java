package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShoppingListDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ShoppingListMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class ShoppingListMappingTest implements TestData {

    private final ShoppingList shoppingList = new ShoppingList(null, TEST_SHOPPINGLIST_NAME,
        TEST_SHOPPINGLIST_NOTES, null, null);
    private final ShoppingListDto shoppingListDto = new ShoppingListDto(null, TEST_SHOPPINGLIST_NAME,
        TEST_SHOPPINGLIST_NOTES, null, null);

    @Autowired
    ShoppingListMapper shoppingListMapper;

    @Test
    public void givenNothing_whenMapShoppingListEntityToDto_thenEntityHasAllProperties() {
        ShoppingListDto shoppingListDto = shoppingListMapper.shoppingListToShoppingListDto(shoppingList);
        assertAll(
            () -> assertEquals(TEST_SHOPPINGLIST_NAME, shoppingListDto.getName()),
            () -> assertEquals(TEST_SHOPPINGLIST_NOTES, shoppingListDto.getNotes())
        );
    }

    @Test
    public void givenNothing_whenMapListShoppingListEntityToDto_thenEntityHasAllProperties() {
        List<ShoppingList> shoppingLists = new ArrayList<>();
        shoppingLists.add(shoppingList);
        shoppingLists.add(shoppingList);

        List<ShoppingListDto> shoppingListDtos = shoppingListMapper.shoppingListToShoppingListDto(shoppingLists);
        assertEquals(2, shoppingListDtos.size());

        ShoppingListDto shoppingListDto = shoppingListDtos.get(0);
        assertAll(
            () -> assertEquals(TEST_SHOPPINGLIST_NAME, shoppingListDto.getName()),
            () -> assertEquals(TEST_SHOPPINGLIST_NOTES, shoppingListDto.getNotes())
        );
    }

    @Test
    public void givenNothing_whenMapShoppingListDtoToEntity_thenEntityHasAllProperties() {
        ShoppingList shoppingList = shoppingListMapper.shoppingListDtoToShoppingList(shoppingListDto);

        assertAll(
            () -> assertEquals(TEST_SHOPPINGLIST_NAME, shoppingList.getName()),
            () -> assertEquals(TEST_SHOPPINGLIST_NOTES, shoppingList.getNotes())
        );
    }
}
