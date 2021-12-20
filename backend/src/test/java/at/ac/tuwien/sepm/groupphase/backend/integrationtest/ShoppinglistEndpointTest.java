package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemStorageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ItemMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ItemStorageMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserLoginMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import at.ac.tuwien.sepm.groupphase.backend.entity.Storage;
import at.ac.tuwien.sepm.groupphase.backend.entity.UserGroup;
import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.Location;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemStorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.StorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserGroupRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Optional;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.SHOPPINGLISTENPOINDT_URI;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.STORAGEENDPOINT_URI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ShoppinglistEndpointTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ShoppingListRepository shoppingListRepository;
    @Autowired
    private ItemStorageRepository itemStorageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserGroupRepository userGroupRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ItemStorageMapper itemStorageMapper;
    @Autowired
    UserLoginMapper userLoginMapper;

    ShoppingList shoppingList;

    Set<ItemStorage> itemList;

    ApplicationUser user;

    UserGroup userGroup;

    ItemStorage mushrooms;

    ItemStorage pasta;

    @BeforeEach
    void beforeEach() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto("test", "password", "test@email.com");
        Optional<ApplicationUser> applicationUser = userRepository.findUserByUsername(userRegistrationDto.getUsername());
        if (applicationUser.isEmpty()) {
            Long publicShoppingListId = -1L;
            Long publicStorageId = -1L;
            UserGroup group = new UserGroup(publicStorageId, publicShoppingListId);
            group = userGroupRepository.saveAndFlush(group);
            user = new ApplicationUser();
            user = userLoginMapper.dtoToEntity(userRegistrationDto, -1L);
            user.setCurrGroup(group);
            userRepository.saveAndFlush(user);

        }

        Set<ApplicationUser> userSet = new HashSet<>();
        userSet.add(user);

        userGroup = new UserGroup();
        userGroup = userGroupRepository.saveAndFlush(userGroup);
        userGroup.setUser(userSet);
        userGroup = userGroupRepository.saveAndFlush(userGroup);
        userGroup.setStorageId(-1L);
        userGroup = userGroupRepository.saveAndFlush(userGroup);

        user.setCurrGroup(userGroup);
        user = userRepository.saveAndFlush(user);

        shoppingList = ShoppingList.ShoppingListBuilder.aShoppingList()
            .withName("Test")
            .withOwner(user)
            .withNotes("test notes")
            .withItems(null)
            .build();
        shoppingList = shoppingListRepository.saveAndFlush(shoppingList);

        mushrooms = new ItemStorage("Mushrooms", null, null, null, 200,
            null, null, null, shoppingList.getId());
        itemStorageRepository.save(mushrooms);
        pasta = new ItemStorage("Pasta", null, null, null, 500,
            null, null, null, shoppingList.getId());
        itemStorageRepository.save(pasta);

        itemList = new HashSet<>();
        itemList.add(mushrooms);
        itemList.add(pasta);

        shoppingList.setItems(itemList);
        shoppingListRepository.saveAndFlush(shoppingList);
    }

    @Test
    public void workOffShoppingList_ShouldReturn_shoppingListWithEmptyItems() throws Exception {
        ItemStorageDto mushroomsDto = itemStorageMapper.itemStorageToItemStorageDto(mushrooms);
        ItemStorageDto pastaDto = itemStorageMapper.itemStorageToItemStorageDto(pasta);

        List<ItemStorageDto> itemsToBuy = new LinkedList<ItemStorageDto>();
        itemsToBuy.add(mushroomsDto);
        itemsToBuy.add(pastaDto);

        System.out.println("shoppingListId: " + shoppingList.getId() + ", username: " + user.getUsername());

        MvcResult mvcResult = this.mockMvc.perform(put(SHOPPINGLISTENPOINDT_URI + "/" + shoppingList.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemsToBuy)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        //assertEquals(HttpStatus.OK.value(), response.getStatus());
        Set emptySet = new HashSet<ItemStorage>();
        assertTrue(shoppingList.getItems().isEmpty());
    }
}
