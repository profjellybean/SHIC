package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.RecipeDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.TestDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemStorageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ItemStorageMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ShoppingListMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserLoginMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import at.ac.tuwien.sepm.groupphase.backend.entity.UserGroup;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemStorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserGroupRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
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

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

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
    RecipeRepository recipeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ShoppingListMapper shoppingListMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    TestDataGenerator testDataGenerator;

    @Autowired
    private ItemStorageRepository itemStorageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserGroupRepository userGroupRepository;
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

    //@Autowired
    //RecipeDataGenerator recipeDataGenerator;

/*
    @BeforeEach
    public void beforeEach() {
        Optional<ApplicationUser> userOptional = userRepository.findUserByUsername(ADMIN_USER);
        if(userOptional.isPresent()) {
            ApplicationUser user = userOptional.get();
        }
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

 */
    /*
    @AfterEach
    public void afterEach() {
        recipeRepository.deleteAll();
    }
     */

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
    public void givenNoRecipe_whenPlanRecipe_then400() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(put(SHOPPINGLIST_ENDPOINT_URI)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            //.andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

    }

    @Test
    public void givenInvalidRecipeId_whenPlanRecipe_then404() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(put(SHOPPINGLIST_ENDPOINT_URI)
                .param("recipeId", "-1")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    //@Test
    public void givenValidRecipe_notEnoughOfIngredient_whenPlanRecipe_then400() throws Exception {
        testDataGenerator.generateData_planRecipe();
        Recipe recipe = recipeRepository.findByName("Feta Cheese Noodles");

        MvcResult mvcResult = this.mockMvc.perform(put(SHOPPINGLIST_ENDPOINT_URI)
                .param("recipeId", recipe.getId().toString())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<ItemStorageDto> itemStorageDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            ItemStorageDto[].class));

        assertEquals(1, itemStorageDtos.size());
        ItemStorageDto itemStorageDto1 = itemStorageDtos.get(0);
        assertAll(
            () -> assertEquals("Feta", itemStorageDto1.getName()),
            () -> assertEquals(2, itemStorageDto1.getAmount()),
            () -> assertEquals("pieces", itemStorageDto1.getQuantity().getName()) // TODO
        );
    }

    @Test
    public void givenValidRecipe_allIngredientsMissing_whenPlanRecipe_then400() throws Exception {
        testDataGenerator.generateData_planRecipe();
        Recipe recipe = recipeRepository.findByName("Potato Wedges");

        MvcResult mvcResult = this.mockMvc.perform(put(SHOPPINGLIST_ENDPOINT_URI)
                .param("recipeId", recipe.getId().toString())
                //.param("recipeId", "2")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<ItemStorageDto> itemStorageDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            ItemStorageDto[].class));

        assertEquals(1, itemStorageDtos.size());
        ItemStorageDto itemStorageDto = itemStorageDtos.get(0);
        assertAll(
            () -> assertEquals("Potatoes", itemStorageDto.getName()),
            () -> assertEquals("any kind", itemStorageDto.getNotes()),
            () -> assertEquals(400, itemStorageDto.getAmount()),
            () -> assertEquals("g", itemStorageDto.getQuantity().getName())
        );

    }

    //@Test
    public void givenValidRecipe_allIngredientsPresent_whenPlanRecipe_then400() throws Exception {
        testDataGenerator.generateData_planRecipe();
        Recipe recipe = recipeRepository.findByName("Noodles with Pesto");

        MvcResult mvcResult = this.mockMvc.perform(put(SHOPPINGLIST_ENDPOINT_URI)
                .param("recipeId", recipe.getId().toString())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<ItemStorageDto> itemStorageDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            ItemStorageDto[].class));

        assertEquals(0, itemStorageDtos.size());

    }
/*
    @Test
    public void workOffShoppingList_ShouldReturn_shoppingListWithEmptyItems() throws Exception {

        ItemStorageDto mushroomsDto = itemStorageMapper.itemStorageToItemStorageDto(mushrooms);
        ItemStorageDto pastaDto = itemStorageMapper.itemStorageToItemStorageDto(pasta);

        List<ItemStorageDto> itemsToBuy = new LinkedList<ItemStorageDto>();
        itemsToBuy.add(mushroomsDto);
        itemsToBuy.add(pastaDto);

        MvcResult mvcResult = this.mockMvc.perform(put(SHOPPINGLISTENPOINDT_URI + "/" + shoppingList.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemsToBuy))
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        //assertEquals(HttpStatus.OK.value(), response.getStatus());
        Set emptySet = new HashSet<ItemStorage>();
        ShoppingList workedOffList = shoppingListRepository.getById(shoppingList.getId());
        assertEquals(emptySet, workedOffList.getItems());
    }

 */


/*
    @Test
    public void workOffShoppingList_WithNoItems_ShouldThrowError() throws Exception {


        MvcResult mvcResult = this.mockMvc.perform(put(SHOPPINGLISTENPOINDT_URI + "/" + shoppingList.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemsToBuy))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(user.getUsername(), USER_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        //assertEquals(HttpStatus.OK.value(), response.getStatus());
        Set emptySet = new HashSet<ItemStorage>();
        ShoppingList workedOffList = shoppingListRepository.getById(shoppingList.getId());
        assertEquals(emptySet, workedOffList.getItems());
    }
    */

}
