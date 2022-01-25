package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.TestDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemStorageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ItemStorageMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserLoginMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemStorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepm.groupphase.backend.service.ShoppingListService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ShoppinglistEndpointTest implements TestData {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ShoppingListRepository shoppingListRepository;
    @Autowired
    ItemStorageRepository itemStorageRepository;
    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtTokenizer jwtTokenizer;
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    TestDataGenerator testDataGenerator;
    @Autowired
    private ItemStorageMapper itemStorageMapper;
    @Autowired
    UserLoginMapper userLoginMapper;
    @Autowired
    ShoppingListService shoppingListService;


    @BeforeEach
    public void beforeEach() {

    }

    @AfterEach
    public void afterEach() {
        recipeRepository.deleteAll();
    }


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

    /* Rezept fehlt
    @Test
    public void insertValidItemToShoppingList() throws Exception {
        ItemStorageDto itemStorageDto = new ItemStorageDto(TEST_ITEMSTORAGE_NAME, 2L);
        shoppingListRepository.saveAndFlush(new ShoppingList(2L,TEST_SHOPPINGLIST_NAME));

        MvcResult mvcResult = this.mockMvc.perform(post(SHOPPINGLIST_ENDPOINT_URI + "/newItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemStorageDto)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
*/


    @Test
    public void givenNoRecipe_whenPlanRecipe_then400() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(put(SHOPPINGLIST_ENDPOINT_URI)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, USER_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

    }

    @Test
    public void givenInvalidRecipeId_whenPlanRecipe_then404() throws Exception {
        testDataGenerator.generateData_generateUser_withGroup();

        MvcResult mvcResult = this.mockMvc.perform(put(SHOPPINGLIST_ENDPOINT_URI)
                .param("recipeId", "-1")
                .param("numberOfPeople", "1")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, USER_ROLES)))
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void givenNoNumberOfPeople_whenPlanRecipe_then400() throws Exception {
        testDataGenerator.generateData_planRecipe_allIngredientsMissing();
        Recipe recipe = recipeRepository.findByName("testRecipe");

        MvcResult mvcResult = this.mockMvc.perform(put(SHOPPINGLIST_ENDPOINT_URI)
                .param("recipeId", recipe.getId().toString())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, USER_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void givenInvalidNumberOfPeople_whenPlanRecipe_then422() throws Exception {
        testDataGenerator.generateData_planRecipe_allIngredientsPresent();
        Recipe recipe = recipeRepository.findByName("testRecipe");

        MvcResult mvcResult = this.mockMvc.perform(put(SHOPPINGLIST_ENDPOINT_URI)
                .param("recipeId", recipe.getId().toString())
                .param("numberOfPeople", "0")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, USER_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
    }

    @Test
    public void givenToHighNumberOfPeople_whenPlanRecipe_then422() throws Exception {
        testDataGenerator.generateData_planRecipe_allIngredientsPresent();
        Recipe recipe = recipeRepository.findByName("testRecipe");

        MvcResult mvcResult = this.mockMvc.perform(put(SHOPPINGLIST_ENDPOINT_URI)
                .param("recipeId", recipe.getId().toString())
                .param("numberOfPeople", "100000")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, USER_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
    }

    @Test
    public void givenValidRecipe_notEnoughOfIngredient_whenPlanRecipe_then200() throws Exception {
        testDataGenerator.generateData_planRecipe_notEnoughOfIngredient();
        Recipe recipe = recipeRepository.findByName("testRecipe");

        MvcResult mvcResult = this.mockMvc.perform(put(SHOPPINGLIST_ENDPOINT_URI)
                .param("recipeId", recipe.getId().toString())
                .param("numberOfPeople", "1")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, USER_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<ItemStorageDto> itemStorageDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            ItemStorageDto[].class));
        assertEquals(1, itemStorageDtos.size());
        ItemStorageDto itemStorageDto = itemStorageDtos.get(0);

        assertAll(
            () -> assertEquals("testItem", itemStorageDto.getName()),
            () -> assertEquals("Ingredient for recipe: testRecipe", itemStorageDto.getNotes()),
            () -> assertEquals(9, itemStorageDto.getAmount()),
            () -> assertEquals("testQuantity", itemStorageDto.getQuantity().getName()),
            () -> assertNull(itemStorageDto.getExpDate()),
            () -> assertNull(itemStorageDto.getLocationTag()),
            () -> assertNull(itemStorageDto.getStorageId()),
            () -> assertNotNull((itemStorageDto.getShoppingListId()))
        );

    }

    @Test
    public void givenValidRecipe_allIngredientsMissing_whenPlanRecipe_then200() throws Exception {
        testDataGenerator.generateData_planRecipe_allIngredientsMissing();
        Recipe recipe = recipeRepository.findByName("testRecipe");

        MvcResult mvcResult = this.mockMvc.perform(put(SHOPPINGLIST_ENDPOINT_URI)
                .param("recipeId", recipe.getId().toString())
                .param("numberOfPeople", "1")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, USER_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<ItemStorageDto> itemStorageDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            ItemStorageDto[].class));
        assertEquals(1, itemStorageDtos.size());
        ItemStorageDto itemStorageDto = itemStorageDtos.get(0);

        assertAll(
            () -> assertEquals("testItem", itemStorageDto.getName()),
            () -> assertEquals("Ingredient for recipe: testRecipe", itemStorageDto.getNotes()),
            () -> assertEquals(10, itemStorageDto.getAmount()),
            () -> assertEquals("testQuantity", itemStorageDto.getQuantity().getName()),
            () -> assertNull(itemStorageDto.getExpDate()),
            () -> assertNull(itemStorageDto.getLocationTag()),
            () -> assertNull(itemStorageDto.getStorageId()),
            () -> assertNotNull((itemStorageDto.getShoppingListId()))
        );
    }

    @Test
    public void givenValidRecipe_andNumberOfPeople_allIngredientsMissing_whenPlanRecipe_thenAddMoreIngredients_then200() throws Exception {
        testDataGenerator.generateData_planRecipe_allIngredientsMissing();
        Recipe recipe = recipeRepository.findByName("testRecipe");
        int numberOfPeople = 3;

        MvcResult mvcResult = this.mockMvc.perform(put(SHOPPINGLIST_ENDPOINT_URI)
                .param("recipeId", recipe.getId().toString())
                .param("numberOfPeople", String.valueOf(numberOfPeople))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, USER_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<ItemStorageDto> itemStorageDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            ItemStorageDto[].class));
        assertEquals(1, itemStorageDtos.size());
        ItemStorageDto itemStorageDto = itemStorageDtos.get(0);

        assertAll(
            () -> assertEquals("testItem", itemStorageDto.getName()),
            () -> assertEquals("Ingredient for recipe: testRecipe", itemStorageDto.getNotes()),
            () -> assertEquals(10 * numberOfPeople, itemStorageDto.getAmount()),
            () -> assertEquals("testQuantity", itemStorageDto.getQuantity().getName()),
            () -> assertNull(itemStorageDto.getExpDate()),
            () -> assertNull(itemStorageDto.getLocationTag()),
            () -> assertNull(itemStorageDto.getStorageId()),
            () -> assertNotNull((itemStorageDto.getShoppingListId()))
        );
    }

    @Test
    public void givenValidRecipe_allIngredientsPresent_whenPlanRecipe_then200() throws Exception {
        testDataGenerator.generateData_planRecipe_allIngredientsPresent();
        Recipe recipe = recipeRepository.findByName("testRecipe");

        MvcResult mvcResult = this.mockMvc.perform(put(SHOPPINGLIST_ENDPOINT_URI)
                .param("recipeId", recipe.getId().toString())
                .param("numberOfPeople", "1")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, USER_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<ItemStorageDto> itemStorageDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            ItemStorageDto[].class));
        assertEquals(0, itemStorageDtos.size());
    }

    @Test
    public void givenNoRecipe_whenPutRecipeOnShoppingList_then400() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(put(SHOPPINGLIST_ENDPOINT_URI + "/putAllIngredientsOfRecipe")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, USER_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

    }

    @Test
    public void givenInvalidRecipeId_whenPutRecipeOnShoppingList_then404() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(put(SHOPPINGLIST_ENDPOINT_URI + "/putAllIngredientsOfRecipe")
                .param("recipeId", "-1")
                .param("numberOfPeople", "1")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, USER_ROLES)))
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void givenNoNumberOfPeople_whenPutRecipeOnShoppingList_then400() throws Exception {
        testDataGenerator.generateData_planRecipe_allIngredientsMissing();
        Recipe recipe = recipeRepository.findByName("testRecipe");

        MvcResult mvcResult = this.mockMvc.perform(put(SHOPPINGLIST_ENDPOINT_URI + "/putAllIngredientsOfRecipe")
                .param("recipeId", recipe.getId().toString())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, USER_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void givenInvalidNumberOfPeople_whenPutRecipeOnShoppingList_then422() throws Exception {
        testDataGenerator.generateData_planRecipe_allIngredientsMissing();
        Recipe recipe = recipeRepository.findByName("testRecipe");

        MvcResult mvcResult = this.mockMvc.perform(put(SHOPPINGLIST_ENDPOINT_URI + "/putAllIngredientsOfRecipe")
                .param("recipeId", recipe.getId().toString())
                .param("numberOfPeople", "0")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, USER_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
    }

    @Test
    public void givenToHighNumberOfPeople_whenPutRecipeOnShoppingList_then422() throws Exception {
        testDataGenerator.generateData_planRecipe_allIngredientsMissing();
        Recipe recipe = recipeRepository.findByName("testRecipe");

        MvcResult mvcResult = this.mockMvc.perform(put(SHOPPINGLIST_ENDPOINT_URI + "/putAllIngredientsOfRecipe")
                .param("recipeId", recipe.getId().toString())
                .param("numberOfPeople", "100000")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, USER_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
    }

    @Test
    public void givenValidRecipe_allIngredientsPresent_thenStillAddAllIngredients_whenPutRecipeOnShoppingList() throws Exception {
        testDataGenerator.generateData_planRecipe_allIngredientsPresent();
        Recipe recipe = recipeRepository.findByName("testRecipe");

        MvcResult mvcResult = this.mockMvc.perform(put(SHOPPINGLIST_ENDPOINT_URI + "/putAllIngredientsOfRecipe")
                .param("recipeId", recipe.getId().toString())
                .param("numberOfPeople", "1")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, USER_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<ItemStorageDto> itemStorageDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            ItemStorageDto[].class));
        assertEquals(1, itemStorageDtos.size());
        ItemStorageDto itemStorageDto = itemStorageDtos.get(0);
        assertAll(
            () -> assertEquals("testItem", itemStorageDto.getName()),
            () -> assertEquals("Ingredient for recipe: testRecipe", itemStorageDto.getNotes()),
            () -> assertEquals(10, itemStorageDto.getAmount()),
            () -> assertEquals("testQuantity", itemStorageDto.getQuantity().getName()),
            () -> assertNull(itemStorageDto.getExpDate()),
            () -> assertNull(itemStorageDto.getLocationTag()),
            () -> assertNull(itemStorageDto.getStorageId()),
            () -> assertNotNull((itemStorageDto.getShoppingListId()))
        );
    }

    @Test
    public void givenValidRecipe_andNumberOfPeople_thenAddMoreIngredients_whenPutRecipeOnShoppingList() throws Exception {
        testDataGenerator.generateData_planRecipe_allIngredientsPresent();
        Recipe recipe = recipeRepository.findByName("testRecipe");
        int numberOfPeople = 3;

        MvcResult mvcResult = this.mockMvc.perform(put(SHOPPINGLIST_ENDPOINT_URI + "/putAllIngredientsOfRecipe")
                .param("recipeId", recipe.getId().toString())
                .param("numberOfPeople", String.valueOf(numberOfPeople))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, USER_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<ItemStorageDto> itemStorageDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            ItemStorageDto[].class));
        assertEquals(1, itemStorageDtos.size());
        ItemStorageDto itemStorageDto = itemStorageDtos.get(0);
        assertAll(
            () -> assertEquals("testItem", itemStorageDto.getName()),
            () -> assertEquals("Ingredient for recipe: testRecipe", itemStorageDto.getNotes()),
            () -> assertEquals(10 * numberOfPeople, itemStorageDto.getAmount()),
            () -> assertEquals("testQuantity", itemStorageDto.getQuantity().getName()),
            () -> assertNull(itemStorageDto.getExpDate()),
            () -> assertNull(itemStorageDto.getLocationTag()),
            () -> assertNull(itemStorageDto.getStorageId()),
            () -> assertNotNull((itemStorageDto.getShoppingListId()))
        );
    }

    @Test
    public void givenUserWithoutShoppingList_whenPutRecipeOnShoppingList_then404() throws Exception {
        testDataGenerator.generateData_generateUser_withGroup_withOnlyNullValues();
        Recipe recipe = new Recipe(-1L, "givenUserWithoutShoppingList_whenPutRecipeOnShoppingList_then404",
            "recipe for tests", null, null, null);
        recipe = recipeRepository.saveAndFlush(recipe);

        MvcResult mvcResult = this.mockMvc.perform(put(SHOPPINGLIST_ENDPOINT_URI + "/putAllIngredientsOfRecipe")
                .param("recipeId", recipe.getId().toString())
                .param("numberOfPeople", "1")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, USER_ROLES)))
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void workOffShoppingList_ShouldReturn_ItemsWithShoppingListIdEqualNull() throws Exception {

        List<Long> idList = testDataGenerator.generateData_workOffShoppingList_successfully();
        ShoppingList shoppingList = shoppingListRepository.getById(idList.get(0));

        ItemStorageDto mushroomsDto = itemStorageMapper.itemStorageToItemStorageDto(itemStorageRepository.getById(idList.get(1)));
        ItemStorageDto pastaDto = itemStorageMapper.itemStorageToItemStorageDto(itemStorageRepository.getById(idList.get(2)));

        List<ItemStorageDto> itemsToBuy = new LinkedList<ItemStorageDto>();
        itemsToBuy.add(mushroomsDto);
        itemsToBuy.add(pastaDto);

        MvcResult mvcResult = this.mockMvc.perform(put(SHOPPINGLISTENPOINDT_URI + "/" + shoppingList.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemsToBuy))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        ItemStorage mushroomStorage = itemStorageRepository.getById(idList.get(1));
        ItemStorage pastaStorage = itemStorageRepository.getById(idList.get(2));

        assertNull(mushroomStorage.getShoppingListId());
        assertNull(pastaStorage.getShoppingListId());
    }

    @Test
    public void workOffShoppingList_WithNoItems_ShouldReturn400() throws Exception {
        Long shoppingListId = testDataGenerator.generateData_workOffShoppingList_withoutItems();
        ShoppingList shoppingList = shoppingListRepository.getById(shoppingListId);

        MvcResult mvcResult = this.mockMvc.perform(put(SHOPPINGLISTENPOINDT_URI + "/" + shoppingList.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(""))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, USER_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        //assertEquals(HttpStatus.OK.value(), response.getStatus());
        Set emptySet = new HashSet<ItemStorage>();
        ShoppingList workedOffList = shoppingListRepository.getById(shoppingList.getId());

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void changeAmountOfItemOnPublicShoppingList_ShouldReturn_ShoppingListWithItemWithChangedAmount() throws Exception {

        List<Long> idList = testDataGenerator.generateData_changeAmountOfItemOnPublicShoppingList_WithValidItem();
        ShoppingList shoppingList = shoppingListRepository.getById(idList.get(0));

        ItemStorageDto mushroomsDto = itemStorageMapper.itemStorageToItemStorageDto(itemStorageRepository.getById(idList.get(1)));

        mushroomsDto.setAmount(600);

        MvcResult mvcResult = this.mockMvc.perform(put(SHOPPINGLISTENPOINDT_URI + "/public/" + shoppingList.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mushroomsDto))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        ItemStorage mushroomStorage = itemStorageRepository.getById(idList.get(1));

        assertEquals(600, mushroomStorage.getAmount());
    }

    @Test
    public void changeAmountOfItemOnPublicShoppingList_withItemNotOnShoppingList_ShouldThrow_ServiceException() throws Exception {

        List<Long> idList = testDataGenerator.generateData_changeAmountOfItemOnPublicShoppingList_WithValidItem();
        ShoppingList shoppingList = shoppingListRepository.getById(idList.get(0));

        ItemStorageDto pastaDto = new ItemStorageDto(null, "Pasta", null, 500, null);


        MvcResult mvcResult = this.mockMvc.perform(put(SHOPPINGLISTENPOINDT_URI + "/public/" + shoppingList.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pastaDto))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertThrows(ServiceException.class, () -> shoppingListService.changeAmountOfItem(itemStorageMapper.itemStorageDtoToItemStorage(pastaDto), shoppingList.getId()));
    }

    @Test
    public void changeAmountOfItemOnPrivateShoppingList_ShouldReturn_ShoppingListWithItemWithChangedAmount() throws Exception {

        List<Long> idList = testDataGenerator.generateData_changeAmountOfItemOnPrivateShoppingList_WithValidItem();
        ShoppingList shoppingList = shoppingListRepository.getById(idList.get(0));

        ItemStorageDto mushroomsDto = itemStorageMapper.itemStorageToItemStorageDto(itemStorageRepository.getById(idList.get(1)));

        mushroomsDto.setAmount(600);

        MvcResult mvcResult = this.mockMvc.perform(put(SHOPPINGLISTENPOINDT_URI + "/private/" + shoppingList.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mushroomsDto))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        ItemStorage mushroomStorage = itemStorageRepository.getById(idList.get(1));

        assertEquals(600, mushroomStorage.getAmount());
    }

    @Test
    public void changeAmountOfItemOnPrivateShoppingList_withItemNotOnShoppingList_ShouldThrow_ServiceException() throws Exception {

        List<Long> idList = testDataGenerator.generateData_changeAmountOfItemOnPrivateShoppingList_WithValidItem();
        ShoppingList shoppingList = shoppingListRepository.getById(idList.get(0));

        ItemStorageDto pastaDto = new ItemStorageDto(null, "Pasta", null, 500, null);


        MvcResult mvcResult = this.mockMvc.perform(put(SHOPPINGLISTENPOINDT_URI + "/private/" + shoppingList.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pastaDto))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertThrows(ServiceException.class, () -> shoppingListService.changeAmountOfItem(itemStorageMapper.itemStorageDtoToItemStorage(pastaDto), shoppingList.getId()));
    }

}
