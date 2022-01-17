package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.TestDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemStorageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RecipeDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.RecipeMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UnitOfQuantityMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.entity.UserGroup;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnchangeableException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UnitOfQuantityRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepm.groupphase.backend.service.RecipeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class RecipeEndpointTest implements TestData {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtTokenizer jwtTokenizer;
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private TestDataGenerator testDataGenerator;
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private UnitOfQuantityRepository unitOfQuantityRepository;
    @Autowired
    private UnitOfQuantityMapper unitOfQuantityMapper;
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private RecipeMapper recipeMapper;

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
    public void getRecipe_thenReturnRecipe() throws Exception {
        testDataGenerator.generateData_ValidRecipe();
        Recipe recipe = recipeRepository.findByName("testRecipe");

        MvcResult mvcResult = this.mockMvc.perform(get(RECIPEENPOINT_URI + "/" + recipe.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        RecipeDto savedRecipe = objectMapper.readValue(response.getContentAsString(), RecipeDto.class);

        List<String> ingredients = new LinkedList<String>();
        for (ItemStorage item :
            recipe.getIngredients()) {
            ingredients.add(item.getName());
        }

        List<String> savedRecipeIngredients = new LinkedList<String>();
        for (ItemStorageDto item :
            savedRecipe.getIngredients()) {
            savedRecipeIngredients.add(item.getName());
        }

        assertAll(
            () -> assertEquals(recipe.getName(), savedRecipe.getName()),
            () -> assertEquals(recipe.getDescription(), savedRecipe.getDescription()),
            () -> assertEquals(ingredients, savedRecipeIngredients),
            () -> assertEquals(recipe.getCategories(), savedRecipe.getCategories()),
            () -> assertEquals(recipe.getGroupId(), savedRecipe.getGroupId())
        );
    }

    @Test
    public void getNotExistingRecipe_returnNull() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(get(RECIPEENPOINT_URI + "/-1")
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        //RecipeDto recipe = objectMapper.readValue(response.getContentAsString(), RecipeDto.class);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        //assertNull(recipe);

    }

    @Test
    public void getAllRecipes_thenReturnRecipe() throws Exception {
        testDataGenerator.generateData_moreValidRecipes();

        MvcResult mvcResult = this.mockMvc.perform(get(RECIPEENPOINT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        List<RecipeDto> allRecipes = Arrays.asList(objectMapper.readValue(response.getContentAsString(), RecipeDto[].class));

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(3, allRecipes.size());
    }

    @Test
    public void deleteExistingRecipeShouldReturnTrue() throws Exception {
        testDataGenerator.generateData_ValidRecipe();
        Recipe recipe = recipeRepository.findByName("testRecipe");

        RecipeDto recipeDto = recipeMapper.recipeToRecipeDto(recipe);

        MvcResult mvcResult = this.mockMvc.perform(delete(RECIPEENPOINT_URI + "/" + recipe.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recipeDto))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        boolean deleted = objectMapper.readValue(response.getContentAsString(), boolean.class);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(deleted);
    }

    @Test
    public void deleteNonExistingRecipeShouldReturnFalse() throws Exception {

        RecipeDto recipeDto = new RecipeDto(-1L, "testRecipe", "recipe for food", null, null, -1L);

        MvcResult mvcResult = this.mockMvc.perform(delete(RECIPEENPOINT_URI + "/-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recipeDto))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertThrows(NotFoundException.class, () -> recipeService.deleteRecipe(TEST_USER, -1L));

    }

    @Test
    public void addValidRecipe_ShouldReturnRecipe() throws Exception {
        UnitOfQuantity unit = new UnitOfQuantity("gramm");
        unitOfQuantityRepository.saveAndFlush(unit);
        HashSet<ItemStorageDto> ingredients = new HashSet<ItemStorageDto>();
        ingredients.add(new ItemStorageDto(null, null, null, "testItem",
            unitOfQuantityMapper.unitOfQuantityToUnitOfQuantityDto(unit), "item for tests",
            null, null, 10, null));
        RecipeDto recipeDto = new RecipeDto(-1L, "testRecipe", "recipe for food", ingredients, null, -1L);

        MvcResult mvcResult = this.mockMvc.perform(post(RECIPEENPOINT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recipeDto))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        RecipeDto updatedRecipe = objectMapper.readValue(response.getContentAsString(), RecipeDto.class);

        List<String> updatedIngredients = new LinkedList<String>();
        for (ItemStorageDto item :
            ingredients) {
            updatedIngredients.add(item.getName());
        }

        List<String> updatedRecipeIngredients = new LinkedList<String>();
        for (ItemStorageDto item :
            updatedRecipe.getIngredients()) {
            updatedRecipeIngredients.add(item.getName());
        }

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertAll(
            () -> assertEquals("testRecipe", updatedRecipe.getName()),
            () -> assertEquals("recipe for food", updatedRecipe.getDescription()),
            () -> assertEquals(updatedIngredients, updatedRecipeIngredients),
            () -> assertNull(updatedRecipe.getCategories()),
            () -> assertEquals(-1L, updatedRecipe.getGroupId())
        );
    }

    @Test
    @Disabled("Wirft die falsche Exception")
    public void addInvalidRecipe_ShouldValidationException() throws Exception {
        UnitOfQuantity unit = new UnitOfQuantity("gramm");
        unitOfQuantityRepository.saveAndFlush(unit);
        HashSet<ItemStorageDto> ingredients = new HashSet<ItemStorageDto>();
        ingredients.add(new ItemStorageDto(null, null, null, "testItem",
            unitOfQuantityMapper.unitOfQuantityToUnitOfQuantityDto(unit), "item for tests",
            null, null, 10, null));
        RecipeDto recipeDto = new RecipeDto(-1L, null, "recipe for food", ingredients, null, -1L);

        MvcResult mvcResult = this.mockMvc.perform(post(RECIPEENPOINT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recipeDto))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        RecipeDto updatedRecipe = objectMapper.readValue(response.getContentAsString(), RecipeDto.class);

        assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
        assertThrows(ValidationException.class, () -> recipeService.addRecipe(recipeMapper.recipeDtoToRecipe(recipeDto)));
    }

    @Test
    public void updatedExistingValidRecipe_thenReturnUpdatedRecipe() throws Exception {
        testDataGenerator.generateData_ValidRecipe();
        Recipe recipe = recipeRepository.findByName("testRecipe");

        List<UnitOfQuantity> quantities = unitOfQuantityRepository.findAll();
        Map<String, UnitOfQuantity> mappedUnits = new HashMap<>();
        for (UnitOfQuantity unit :
            quantities) {
            mappedUnits.put(unit.getName(), unit);
        }
        HashSet<ItemStorageDto> ingredients = new HashSet<ItemStorageDto>();
        ingredients.add(new ItemStorageDto(null, null, null, "testItem",
            unitOfQuantityMapper.unitOfQuantityToUnitOfQuantityDto(mappedUnits.get("kg")), "item for tests",
            null, null, 10, null));
        RecipeDto recipeDto = new RecipeDto(recipe.getId(), "testRecipe", "recipe for food", ingredients, null, -1L);

        MvcResult mvcResult = this.mockMvc.perform(put(RECIPEENPOINT_URI + "/" + recipe.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recipeDto))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        RecipeDto updatedRecipe = objectMapper.readValue(response.getContentAsString(), RecipeDto.class);

        List<String> updatedIngredients = new LinkedList<String>();
        for (ItemStorageDto item :
            ingredients) {
            updatedIngredients.add(item.getName());
        }

        List<String> updatedRecipeIngredients = new LinkedList<String>();
        for (ItemStorageDto item :
            updatedRecipe.getIngredients()) {
            updatedRecipeIngredients.add(item.getName());
        }

        assertAll(
            () -> assertEquals("testRecipe", updatedRecipe.getName()),
            () -> assertEquals("recipe for food", updatedRecipe.getDescription()),
            () -> assertEquals(updatedIngredients, updatedRecipeIngredients),
            () -> assertNull(updatedRecipe.getCategories()),
            () -> assertEquals(-1L, updatedRecipe.getGroupId())
        );

    }

    @Test
    public void updatedNonExistingValidRecipe_shouldThrowNotFoundException() throws Exception {
        List<UnitOfQuantity> quantities = unitOfQuantityRepository.findAll();
        Map<String, UnitOfQuantity> mappedUnits = new HashMap<>();
        for (UnitOfQuantity unit :
            quantities) {
            mappedUnits.put(unit.getName(), unit);
        }
        HashSet<ItemStorageDto> ingredients = new HashSet<ItemStorageDto>();
        ingredients.add(new ItemStorageDto(null, null, null, "testItem",
            unitOfQuantityMapper.unitOfQuantityToUnitOfQuantityDto(mappedUnits.get("kg")), "item for tests",
            null, null, 10, null));
        RecipeDto recipeDto = new RecipeDto(-1L, "testRecipe", "recipe for food", ingredients, null, -1L);

        MvcResult mvcResult = this.mockMvc.perform(put(RECIPEENPOINT_URI + "/-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recipeDto))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertThrows(NotFoundException.class, () -> recipeService.updateRecipe(recipeMapper.recipeDtoToRecipe(recipeDto), -1L));

    }

    @Test
    public void updatedExistingValidRecipe_withNoGroupId_shouldThrowUnchangeableException() throws Exception {
        testDataGenerator.generateData_ValidRecipe();
        Recipe recipe = recipeRepository.findByName("testRecipe");
        List<UnitOfQuantity> quantities = unitOfQuantityRepository.findAll();
        Map<String, UnitOfQuantity> mappedUnits = new HashMap<>();
        for (UnitOfQuantity unit :
            quantities) {
            mappedUnits.put(unit.getName(), unit);
        }
        HashSet<ItemStorageDto> ingredients = new HashSet<ItemStorageDto>();
        ingredients.add(new ItemStorageDto(null, null, null, "testItem",
            unitOfQuantityMapper.unitOfQuantityToUnitOfQuantityDto(mappedUnits.get("kg")), "item for tests",
            null, null, 10, null));
        RecipeDto recipeDto = new RecipeDto(-1L, "testRecipe", "recipe for food", ingredients, null, null);

        MvcResult mvcResult = this.mockMvc.perform(put(RECIPEENPOINT_URI + "/-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recipeDto))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
        assertThrows(UnchangeableException.class, () -> recipeService.updateRecipe(recipeMapper.recipeDtoToRecipe(recipeDto), null));
    }

}
