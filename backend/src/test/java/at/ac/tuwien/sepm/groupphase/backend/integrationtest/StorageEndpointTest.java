package at.ac.tuwien.sepm.groupphase.backend.integrationtest;


import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.TestDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemStorageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RegisterDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ItemStorageMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserLoginMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.Storage;
import at.ac.tuwien.sepm.groupphase.backend.entity.UserGroup;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemStorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.StorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserGroupRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepm.groupphase.backend.service.StorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.HashSet;
import java.util.List;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.STORAGEENDPOINT_URI;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.RequestEntity.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class StorageEndpointTest implements TestData {

    @Autowired
    private JwtTokenizer jwtTokenizer;
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private StorageRepository storageRepository;
    @Autowired
    private ItemStorageRepository itemStorageRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserGroupRepository userGroupRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    UserLoginMapper userLoginMapper;
    @Autowired
    private ItemStorageMapper itemStorageMapper;
    @Autowired
    TestDataGenerator testDataGenerator;
    @Autowired
    private StorageService storageService;


    @AfterEach
    public void afterEach() {
        storageRepository.deleteAll();
        itemStorageRepository.deleteAll();
    }

    @Test
    public void insertItemWithEmptyOrNullStorageIdShouldThrowException() throws Exception {
        ItemStorageDto itemStorageDto = new ItemStorageDto();

        MvcResult mvcResult = this.mockMvc.perform(post(STORAGEENDPOINT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemStorageDto)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void insertValidItem() throws Exception {
        ItemStorageDto itemStorageDto = new ItemStorageDto(-1L, "Test");
        storageRepository.saveAndFlush(new Storage(-1L));


        MvcResult mvcResult = this.mockMvc.perform(post(STORAGEENDPOINT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemStorageDto))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(itemStorageRepository.findByName("Test").isPresent());
    }


    @Test
    public void insertItemsThenGetAll() throws Exception {
        ItemStorage itemStorageDto = new ItemStorage(-1L, "Test1");
        ItemStorage itemStorageDto1 = new ItemStorage(-1L, "Test2");
        ItemStorage itemStorageDto2 = new ItemStorage(-1L, "Test3");
        storageRepository.saveAndFlush(new Storage(-1L));
        itemStorageRepository.saveAndFlush(itemStorageDto);
        itemStorageRepository.saveAndFlush(itemStorageDto1);
        itemStorageRepository.saveAndFlush(itemStorageDto2);

        MvcResult mvcResult = this.mockMvc.perform(get(STORAGEENDPOINT_URI + "?id=", -1)
                .contentType(MediaType.APPLICATION_JSON))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(3, itemStorageRepository.findAllByStorageId(-1L).size());
    }

    @Test
    public void insertItemStorageAndFindIt() throws Exception {
        storageRepository.saveAndFlush(new Storage(-1L));
        ItemStorage itemStorageDto = new ItemStorage(-1, "Test123");
        itemStorageRepository.saveAndFlush(itemStorageDto);

        MvcResult mvcResult = this.mockMvc.perform(get(STORAGEENDPOINT_URI + "?name=", "Test123")
                .contentType(MediaType.APPLICATION_JSON))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(1, itemStorageRepository.findAllByStorageIdAndNameContainingIgnoreCase(-1L, "Test123").size());
    }

    @Test
    public void tryToFindNonExistingItem() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(STORAGEENDPOINT_URI + "?name=", "Test1234567890")
                .contentType(MediaType.APPLICATION_JSON))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(0, itemStorageRepository.findAllByStorageIdAndNameContainingIgnoreCase(-1L, "Test123456786").size());
    }


    @Test
    public void searchForExistingItem() throws Exception {
        ItemStorageDto itemStorageDto = new ItemStorageDto(-1L, "test2");
        storageRepository.saveAndFlush(new Storage(-1L));

        MvcResult mvcResult = this.mockMvc.perform(get(STORAGEENDPOINT_URI + "/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemStorageDto)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

    }
/*
    @Test
    public void deleteExistingItemShouldReturnDeletedItem() throws Exception {
        Storage storage = new Storage();
        storage = storageRepository.saveAndFlush(storage);
        UserRegistrationDto testUser = new UserRegistrationDto("test", "password", "test.user@email.com");
        UserGroup testGroup = new UserGroup(storage.getId(), null, null, new HashSet<ApplicationUser>(), null);
        testGroup = userGroupRepository.saveAndFlush(testGroup);

        ApplicationUser testApplicationUser = userLoginMapper.dtoToEntity(testUser, null);
        testApplicationUser.setCurrGroup(testGroup);
        userRepository.saveAndFlush(testApplicationUser);

        ItemStorageDto itemStorageDto = new ItemStorageDto(storage.getId(), "test");
        ItemStorage item = itemStorageMapper.itemStorageDtoToItemStorage(itemStorageDto);
        itemStorageRepository.saveAndFlush(item);
        itemStorageDto.setId(item.getId());

        MvcResult mvcResult = this.mockMvc.perform(delete(STORAGEENDPOINT_URI + "?itemId=" + itemStorageDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken("test", ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        ItemStorageDto deletedItemStorageDto = objectMapper.readValue(response.getContentAsString(),
            ItemStorageDto.class);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        Storage finalStorage = storage;
        assertAll(
            () -> assertEquals(finalStorage.getId(), deletedItemStorageDto.getStorageId()),
            () -> assertNull(deletedItemStorageDto.getShoppingListId()),
            () -> assertEquals("test", deletedItemStorageDto.getName()),
            () -> assertNull(deletedItemStorageDto.getQuantity()),
            () -> assertNull(deletedItemStorageDto.getNotes()),
            () -> assertNull(deletedItemStorageDto.getImage()),
            () -> assertEquals(0, deletedItemStorageDto.getAmount()),
            () -> assertNull(deletedItemStorageDto.getLocationTag())
        );
        userRepository.delete(testApplicationUser);
    }


    @Test
    public void deleteNotExistingItemShouldThrowNotFoundException() throws Exception {
        Storage storage = new Storage();
        storage = storageRepository.saveAndFlush(storage);
        UserRegistrationDto testUser = new UserRegistrationDto("test", "password", "test.user@email.com");
        UserGroup testGroup = new UserGroup(storage.getId(), null, null, new HashSet<ApplicationUser>(), null);
        testGroup = userGroupRepository.saveAndFlush(testGroup);

        ApplicationUser testApplicationUser = userLoginMapper.dtoToEntity(testUser, null);
        testApplicationUser.setCurrGroup(testGroup);
        userRepository.saveAndFlush(testApplicationUser);

        ItemStorageDto itemStorageDto = new ItemStorageDto(null, "test");
        ItemStorage item = itemStorageMapper.itemStorageDtoToItemStorage(itemStorageDto);
        itemStorageRepository.saveAndFlush(item);
        itemStorageDto.setId(item.getId());

        MvcResult mvcResult = this.mockMvc.perform(delete(STORAGEENDPOINT_URI + "?itemId=" + itemStorageDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken("test", ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        Storage finalStorage = storage;
        assertThrows(NotFoundException.class, () -> storageService.deleteItemInStorageById(itemStorageDto.getId(), finalStorage.getId()));
        userRepository.delete(testApplicationUser);
    }

    @Test
    public void deleteItemFromNotExistingStorageShouldThrowNotFoundException() throws Exception {
        UserRegistrationDto testUser = new UserRegistrationDto("test", "password", "test.user@email.com");
        UserGroup testGroup = new UserGroup(-1L, null, null, new HashSet<ApplicationUser>(), null);
        testGroup = userGroupRepository.saveAndFlush(testGroup);

        ApplicationUser testApplicationUser = userLoginMapper.dtoToEntity(testUser, null);
        testApplicationUser.setCurrGroup(testGroup);
        userRepository.saveAndFlush(testApplicationUser);

        ItemStorageDto itemStorageDto = new ItemStorageDto(null, "test");
        ItemStorage item = itemStorageMapper.itemStorageDtoToItemStorage(itemStorageDto);
        itemStorageRepository.saveAndFlush(item);
        itemStorageDto.setId(item.getId());

        MvcResult mvcResult = this.mockMvc.perform(delete(STORAGEENDPOINT_URI + "?itemId=" + itemStorageDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken("test", ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertThrows(NotFoundException.class, () -> storageService.deleteItemInStorageById(itemStorageDto.getId(), -2L));
        userRepository.delete(testApplicationUser);
    }

     */

    @Test
    public void updateValidItemShouldReturnUpdatedItem() throws Exception {
        Storage storage = new Storage();
        storage = storageRepository.saveAndFlush(storage);
        UserRegistrationDto testUser = new UserRegistrationDto("test", "password", "test.user@email.com");
        UserGroup testGroup = new UserGroup(storage.getId(), null, null, new HashSet<ApplicationUser>(), null);
        testGroup = userGroupRepository.saveAndFlush(testGroup);

        ApplicationUser testApplicationUser = userLoginMapper.dtoToEntity(testUser, null);
        testApplicationUser.setCurrGroup(testGroup);
        userRepository.saveAndFlush(testApplicationUser);

        ItemStorageDto itemStorageDto = new ItemStorageDto(storage.getId(), "test");
        ItemStorage item = itemStorageMapper.itemStorageDtoToItemStorage(itemStorageDto);
        itemStorageRepository.saveAndFlush(item);
        itemStorageDto.setId(item.getId());

        ItemStorageDto updatedItem = itemStorageDto;
        updatedItem.setAmount(600);
        updatedItem.setName("testUpdate");

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.put(STORAGEENDPOINT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedItem))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken("test", ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        ItemStorageDto updatedItemStorageDto = objectMapper.readValue(response.getContentAsString(),
            ItemStorageDto.class);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        Storage finalStorage = storage;
        assertAll(
            () -> assertEquals(finalStorage.getId(), updatedItemStorageDto.getStorageId()),
            () -> assertNull(updatedItemStorageDto.getShoppingListId()),
            () -> assertEquals("testUpdate", updatedItemStorageDto.getName()),
            () -> assertNull(updatedItemStorageDto.getQuantity()),
            () -> assertNull(updatedItemStorageDto.getNotes()),
            () -> assertNull(updatedItemStorageDto.getImage()),
            () -> assertEquals(600, updatedItemStorageDto.getAmount()),
            () -> assertNull(updatedItemStorageDto.getLocationTag())
        );
        userRepository.delete(testApplicationUser);
    }
/*
    @Test
    public void updateNotExistingItemShouldThrow400() throws Exception {
        Storage storage = new Storage();
        storage = storageRepository.saveAndFlush(storage);
        UserRegistrationDto testUser = new UserRegistrationDto("test", "password", "test.user@email.com");
        UserGroup testGroup = new UserGroup(storage.getId(), null, null, new HashSet<ApplicationUser>(), null);
        testGroup = userGroupRepository.saveAndFlush(testGroup);

        ApplicationUser testApplicationUser = userLoginMapper.dtoToEntity(testUser, null);
        testApplicationUser.setCurrGroup(testGroup);
        userRepository.saveAndFlush(testApplicationUser);

        ItemStorageDto itemStorageDto = new ItemStorageDto(null, "test");
        ItemStorage item = itemStorageMapper.itemStorageDtoToItemStorage(itemStorageDto);
        itemStorageRepository.saveAndFlush(item);
        itemStorageDto.setId(item.getId());

        ItemStorageDto updatedItem = itemStorageDto;
        updatedItem.setAmount(600);
        updatedItem.setName("testUpdate");

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.put(STORAGEENDPOINT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedItem))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken("test", ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

 */

    @Test
    public void updateItemFromNotExistingStorageShouldThrowNotFoundException() throws Exception {
        UserRegistrationDto testUser = new UserRegistrationDto("test", "password", "test.user@email.com");
        UserGroup testGroup = new UserGroup(-1L, null, null, new HashSet<ApplicationUser>(), null);
        testGroup = userGroupRepository.saveAndFlush(testGroup);

        ApplicationUser testApplicationUser = userLoginMapper.dtoToEntity(testUser, null);
        testApplicationUser.setCurrGroup(testGroup);
        userRepository.saveAndFlush(testApplicationUser);

        ItemStorageDto itemStorageDto = new ItemStorageDto(-2L, "test");
        ItemStorage item = itemStorageMapper.itemStorageDtoToItemStorage(itemStorageDto);
        itemStorageRepository.saveAndFlush(item);
        itemStorageDto.setId(item.getId());

        ItemStorageDto updatedItem = itemStorageDto;
        updatedItem.setAmount(600);
        updatedItem.setName("testUpdate");

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.put(STORAGEENDPOINT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedItem))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken("test", ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        UserGroup finalTestGroup = testGroup;
        assertThrows(NotFoundException.class, () -> storageService.updateItem(itemStorageMapper.itemStorageDtoToItemStorage(updatedItem), finalTestGroup.getId()));
        userRepository.delete(testApplicationUser);
    }


}
