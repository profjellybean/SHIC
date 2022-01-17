package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemStorageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ItemStorageMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserLoginMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepm.groupphase.backend.entity.Register;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import at.ac.tuwien.sepm.groupphase.backend.entity.Storage;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.entity.UserGroup;
import at.ac.tuwien.sepm.groupphase.backend.repository.BillRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemStorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RegisterRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.StorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UnitOfQuantityRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserGroupRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * class used to generate Data for specific test cases.
 */
@Component
public class TestDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final RecipeDataGenerator recipeDataGenerator;
    private final ShoppingListDataGenerator shoppingListDataGenerator;
    private final StorageDataGenerator storageDataGenerator;
    private final UserDataGenerator userDataGenerator;
    private final ItemStorageDataGenerator itemStorageDataGenerator;
    private final ItemDataGenerator itemDataGenerator;
    private final UnitOfQuantityDataGenerator unitOfQuantityDataGenerator;
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
    @Autowired
    BillRepository billRepository;
    @Autowired
    RegisterRepository registerRepository;
    @Autowired
    UnitOfQuantityRepository unitOfQuantityRepository;
    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    ShoppingListRepository shoppingListRepository;
    @Autowired
    StorageRepository storageRepository;

    public TestDataGenerator(RecipeDataGenerator recipeDataGenerator,
                             ShoppingListDataGenerator shoppingListDataGenerator,
                             StorageDataGenerator storageDataGenerator,
                             UserDataGenerator userDataGenerator,
                             ItemStorageDataGenerator itemStorageDataGenerator,
                             UnitOfQuantityDataGenerator unitOfQuantityDataGenerator,
                             UnitOfQuantityRepository unitOfQuantityRepository,
                             ItemDataGenerator itemDataGenerator) {
        this.recipeDataGenerator = recipeDataGenerator;
        this.shoppingListDataGenerator = shoppingListDataGenerator;
        this.storageDataGenerator = storageDataGenerator;
        this.userDataGenerator = userDataGenerator;
        this.itemStorageDataGenerator = itemStorageDataGenerator;
        this.itemDataGenerator = itemDataGenerator;
        this.unitOfQuantityDataGenerator = unitOfQuantityDataGenerator;
    }

    /**
     * generates Data used in Tests for following method.
     * planRecipe(Long recipeId, String userName) method
     * in ShoppingListService.
     */
    public void generateData_planRecipe_allIngredientsMissing() {
        LOGGER.debug("Generating Data for planning Recipe");
        UserRegistrationDto testUser = new UserRegistrationDto("testUser", "password", "test.user@email.com");

        ShoppingList shoppingList = new ShoppingList();
        shoppingList = shoppingListRepository.save(shoppingList);
        UserGroup testGroup = new UserGroup(-1L, shoppingList.getId(), null, new HashSet<ApplicationUser>(), null);
        testGroup = userGroupRepository.saveAndFlush(testGroup);

        ApplicationUser testApplicationUser = userLoginMapper.dtoToEntity(testUser, null);
        testApplicationUser.setCurrGroup(testGroup);
        userRepository.saveAndFlush(testApplicationUser);

        unitOfQuantityDataGenerator.generateUnitOfQuantity();
        List<UnitOfQuantity> quantities = unitOfQuantityRepository.findAll();
        Map<String, UnitOfQuantity> mappedUnits = new HashMap<>();
        for (UnitOfQuantity unit :
            quantities) {
            mappedUnits.put(unit.getName(), unit);
        }
        HashSet<ItemStorage> ingredients = new HashSet<ItemStorage>();
        ingredients.add(new ItemStorage("testItem", "item for tests", null, null,
            10, null, mappedUnits.get("g"), null, null));
        Recipe recipe = new Recipe(-1L, "testRecipe", "recipe for tests", ingredients, null, null);
        recipeRepository.saveAndFlush(recipe);
    }

    /**
     * generates Data used in Tests for following method.
     * planRecipe(Long recipeId, String userName) method
     * in ShoppingListService.
     */
    public void generateData_planRecipe_allIngredientsPresent() {
        LOGGER.debug("Generating Data for planning Recipe");
        generateData_planRecipe_allIngredientsMissing();

        List<UnitOfQuantity> quantities = unitOfQuantityRepository.findAll();
        Map<String, UnitOfQuantity> mappedUnits = new HashMap<>();
        for (UnitOfQuantity unit :
            quantities) {
            mappedUnits.put(unit.getName(), unit);
        }
        itemStorageRepository.saveAndFlush(new ItemStorage("testItem", "item for tests", null, null,
            10, null, mappedUnits.get("g"), -1L, null));
    }

    /**
     * generates Data used in Tests for following method.
     * planRecipe(Long recipeId, String userName) method
     * in ShoppingListService.
     */
    public void generateData_planRecipe_notEnoughOfIngredient() {
        LOGGER.debug("Generating Data for planning Recipe");
        generateData_planRecipe_allIngredientsMissing();

        List<UnitOfQuantity> quantities = unitOfQuantityRepository.findAll();
        Map<String, UnitOfQuantity> mappedUnits = new HashMap<>();
        for (UnitOfQuantity unit :
            quantities) {
            mappedUnits.put(unit.getName(), unit);
        }
        itemStorageRepository.saveAndFlush(new ItemStorage("testItem", "item for tests", null, null,
            1, null, mappedUnits.get("g"), -1L, null));
    }


    /**
     * generates Data used in Tests for following method.
     * billSumOfCurrentMonth(String userName)
     * in RegisterService
     */
    public void generateData_billSumOfCurrentMonth() {
        LOGGER.debug("Generating Data for sum of Bills in current month");

        Bill bill1 = Bill.BillBuilder.aBill()
            .withRegisterId(-1L)
            .withDate(LocalDate.now())
            .withSum(10)
            .build();
        Bill bill2 = Bill.BillBuilder.aBill()
            .withRegisterId(-1L)
            .withDate(LocalDate.now())
            .withSum(20)
            .build();
        billRepository.saveAndFlush(bill1);
        billRepository.saveAndFlush(bill2);

        UserRegistrationDto testUser = new UserRegistrationDto("testUser", "password", "test.user@email.com");

        UserGroup testGroup = new UserGroup(null, null, -1L, new HashSet<ApplicationUser>(), null);
        testGroup = userGroupRepository.saveAndFlush(testGroup);

        ApplicationUser testApplicationUser = userLoginMapper.dtoToEntity(testUser, null);
        testApplicationUser.setCurrGroup(testGroup);
        userRepository.saveAndFlush(testApplicationUser);

        Set<ApplicationUser> users = testGroup.getUser();
        users.add(testApplicationUser);
        testGroup.setUser(users);
        userGroupRepository.saveAndFlush(testGroup);

    }

    /**
     * generates Data used in Tests for following method.
     * billSumOfCurrentMonth(String userName)
     * in RegisterService
     */
    public void generateData_billSumOfCurrentMonth_noBills() {
        LOGGER.debug("Generating Data for sum of Bills in current month (no Bills)");

        UserRegistrationDto testUser = new UserRegistrationDto("testUser", "password", "test.user@email.com");

        UserGroup testGroup = new UserGroup(null, null, -1L, new HashSet<ApplicationUser>(), null);
        testGroup = userGroupRepository.saveAndFlush(testGroup);

        ApplicationUser testApplicationUser = userLoginMapper.dtoToEntity(testUser, null);
        testApplicationUser.setCurrGroup(testGroup);
        userRepository.saveAndFlush(testApplicationUser);

        Set<ApplicationUser> users = testGroup.getUser();
        users.add(testApplicationUser);
        testGroup.setUser(users);
        userGroupRepository.saveAndFlush(testGroup);

    }

    /**
     * generates Data used in Tests for following method.
     * editMonthlyBudget(Double newBudget, String userName)
     * in RegisterService
     *
     * @return id of generated Register
     */
    public Long generateData_editMonthlyBudget_inRegister() {
        LOGGER.debug("Generating Data for editing Monthly Budget");

        UserRegistrationDto testUser = new UserRegistrationDto("testUser", "password", "test.user@email.com");

        Register register = new Register(-1L, null, 0.0, 200.0);
        register = registerRepository.saveAndFlush(register);

        UserGroup testGroup = new UserGroup(null, null, register.getId(), new HashSet<ApplicationUser>(), null);
        testGroup = userGroupRepository.saveAndFlush(testGroup);

        ApplicationUser testApplicationUser = userLoginMapper.dtoToEntity(testUser, null);
        testApplicationUser.setCurrGroup(testGroup);
        userRepository.saveAndFlush(testApplicationUser);

        Set<ApplicationUser> users = testGroup.getUser();
        users.add(testApplicationUser);
        testGroup.setUser(users);
        userGroupRepository.saveAndFlush(testGroup);

        return register.getId();
    }

    public void generateData_generateUser_withGroup_withOnlyNullValues() {
        LOGGER.debug("Generating Data for editing Monthly Budget");

        UserRegistrationDto testUser = new UserRegistrationDto("testUser", "password", "test.user@email.com");

        UserGroup testGroup = new UserGroup(null, null, null, new HashSet<ApplicationUser>(), "TestGroup");
        testGroup = userGroupRepository.saveAndFlush(testGroup);

        ApplicationUser testApplicationUser = userLoginMapper.dtoToEntity(testUser, null);
        testApplicationUser.setCurrGroup(testGroup);
        userRepository.saveAndFlush(testApplicationUser);

        Set<ApplicationUser> users = testGroup.getUser();
        users.add(testApplicationUser);
        testGroup.setUser(users);
        userGroupRepository.saveAndFlush(testGroup);
    }

    public void generateData_generateUser_withGroup() {
        LOGGER.debug("Generating User with Group");

        Storage storage = new Storage();
        storage = storageRepository.saveAndFlush(storage);
        ShoppingList shoppingList = new ShoppingList();
        shoppingList = shoppingListRepository.saveAndFlush(shoppingList);
        Register register = new Register();
        register = registerRepository.saveAndFlush(register);

        UserRegistrationDto testUser = new UserRegistrationDto("testUser", "password", "test.user@email.com");
        UserGroup testGroup = new UserGroup(-1L, shoppingList.getId(), register.getId(), new HashSet<ApplicationUser>(), null);
        testGroup = userGroupRepository.saveAndFlush(testGroup);

        ApplicationUser testApplicationUser = userLoginMapper.dtoToEntity(testUser, null);
        testApplicationUser.setCurrGroup(testGroup);
        userRepository.saveAndFlush(testApplicationUser);

        /*
        Set<ApplicationUser> users = testGroup.getUser();
        users.add(testApplicationUser);
        testGroup.setUser(users);
        userGroupRepository.saveAndFlush(testGroup);

         */
    }

    public List<Long> generateData_workOffShoppingList_successfully() {
        UserRegistrationDto testUser = new UserRegistrationDto("testUser", "password", "test.user@email.com");

        ShoppingList shoppingList = new ShoppingList();
        shoppingList = shoppingListRepository.save(shoppingList);
        UserGroup testGroup = new UserGroup(-1L, shoppingList.getId(), null, new HashSet<ApplicationUser>(), null);
        testGroup = userGroupRepository.saveAndFlush(testGroup);

        ApplicationUser testApplicationUser = userLoginMapper.dtoToEntity(testUser, null);
        testApplicationUser.setCurrGroup(testGroup);
        userRepository.saveAndFlush(testApplicationUser);

        shoppingList.setName("Test");
        shoppingList.setOwner(testApplicationUser);
        shoppingList.setNotes("test notes");
        shoppingList = shoppingListRepository.saveAndFlush(shoppingList);

        ItemStorage mushrooms = new ItemStorage("Mushrooms", null, null, null, 200,
            null, null, null, shoppingList.getId());
        itemStorageRepository.save(mushrooms);
        ItemStorage pasta = new ItemStorage("Pasta", null, null, null, 500,
            null, null, null, shoppingList.getId());
        itemStorageRepository.save(pasta);

        Set<ItemStorage> itemList = new HashSet<>();
        itemList.add(mushrooms);
        itemList.add(pasta);

        shoppingList.setItems(itemList);
        shoppingListRepository.saveAndFlush(shoppingList);

        List<Long> idList = new LinkedList<>();
        idList.add(shoppingList.getId());
        idList.add(mushrooms.getId());
        idList.add(pasta.getId());

        return idList;
    }

    public Long generateData_workOffShoppingList_withoutItems() {
        UserRegistrationDto testUser = new UserRegistrationDto("testUser", "password", "test.user@email.com");

        ShoppingList shoppingList = new ShoppingList();
        shoppingList = shoppingListRepository.save(shoppingList);
        UserGroup testGroup = new UserGroup(-1L, shoppingList.getId(), null, new HashSet<ApplicationUser>(), null);
        testGroup = userGroupRepository.saveAndFlush(testGroup);

        ApplicationUser testApplicationUser = userLoginMapper.dtoToEntity(testUser, null);
        testApplicationUser.setCurrGroup(testGroup);
        userRepository.saveAndFlush(testApplicationUser);

        shoppingList.setName("Test");
        shoppingList.setOwner(testApplicationUser);
        shoppingList.setNotes("test notes");
        shoppingList = shoppingListRepository.saveAndFlush(shoppingList);

        return shoppingList.getId();
    }

    public List<Long> generateData_changeAmountOfItemOnPublicShoppingList_WithValidItem() {
        UserRegistrationDto testUser = new UserRegistrationDto("testUser", "password", "test.user@email.com");

        ShoppingList shoppingList = new ShoppingList();
        shoppingList = shoppingListRepository.save(shoppingList);
        UserGroup testGroup = new UserGroup(-1L, shoppingList.getId(), null, new HashSet<ApplicationUser>(), null);
        testGroup = userGroupRepository.saveAndFlush(testGroup);

        ApplicationUser testApplicationUser = userLoginMapper.dtoToEntity(testUser, null);
        testApplicationUser.setCurrGroup(testGroup);
        userRepository.saveAndFlush(testApplicationUser);

        shoppingList.setName("Test");
        shoppingList.setOwner(testApplicationUser);
        shoppingList.setNotes("test notes");
        shoppingList = shoppingListRepository.saveAndFlush(shoppingList);

        ItemStorage mushrooms = new ItemStorage("Mushrooms", null, null, null, 200,
            null, null, null, shoppingList.getId());
        itemStorageRepository.save(mushrooms);
        ItemStorage pasta = new ItemStorage("Pasta", null, null, null, 500,
            null, null, null, shoppingList.getId());
        itemStorageRepository.save(pasta);

        Set<ItemStorage> itemList = new HashSet<>();
        itemList.add(mushrooms);
        itemList.add(pasta);

        shoppingList.setItems(itemList);
        shoppingListRepository.saveAndFlush(shoppingList);

        List<Long> idList = new LinkedList<>();
        idList.add(shoppingList.getId());
        idList.add(mushrooms.getId());

        return idList;
    }

    public List<Long> generateData_changeAmountOfItemOnPrivateShoppingList_WithValidItem() {
        ShoppingList shoppingList = new ShoppingList();
        shoppingList = shoppingListRepository.save(shoppingList);
        UserRegistrationDto testUser = new UserRegistrationDto("testUser", "password", "test.user@email.com");
        UserGroup testGroup = new UserGroup(-1L, shoppingList.getId(), null, new HashSet<ApplicationUser>(), null);
        testGroup = userGroupRepository.saveAndFlush(testGroup);

        ShoppingList privateShoppingList = new ShoppingList();
        privateShoppingList = shoppingListRepository.save(privateShoppingList);

        ApplicationUser testApplicationUser = userLoginMapper.dtoToEntity(testUser, null);
        testApplicationUser.setCurrGroup(testGroup);
        testApplicationUser.setPrivList(privateShoppingList.getId());
        userRepository.saveAndFlush(testApplicationUser);

        privateShoppingList.setName("Test");
        privateShoppingList.setOwner(testApplicationUser);
        privateShoppingList.setNotes("test notes");
        privateShoppingList = shoppingListRepository.saveAndFlush(privateShoppingList);

        ItemStorage mushrooms = new ItemStorage("Mushrooms", null, null, null, 200,
            null, null, null, privateShoppingList.getId());
        itemStorageRepository.save(mushrooms);
        ItemStorage pasta = new ItemStorage("Pasta", null, null, null, 500,
            null, null, null, privateShoppingList.getId());
        itemStorageRepository.save(pasta);

        Set<ItemStorage> itemList = new HashSet<>();
        itemList.add(mushrooms);
        itemList.add(pasta);

        privateShoppingList.setItems(itemList);
        shoppingListRepository.saveAndFlush(privateShoppingList);

        List<Long> idList = new LinkedList<>();
        idList.add(privateShoppingList.getId());
        idList.add(mushrooms.getId());

        return idList;
    }

    /**
     * generates Data used in Tests for following method.
     * planRecipe(Long recipeId, String userName) method
     * in ShoppingListService.
     */
    public void generateData_editExpistingRecipe() {
        LOGGER.debug("Generating Data for planning Recipe");
        UserRegistrationDto testUser = new UserRegistrationDto("testUser", "password", "test.user@email.com");

        ShoppingList shoppingList = new ShoppingList();
        shoppingList = shoppingListRepository.save(shoppingList);
        UserGroup testGroup = new UserGroup(-1L, shoppingList.getId(), null, new HashSet<ApplicationUser>());
        testGroup = userGroupRepository.saveAndFlush(testGroup);

        ApplicationUser testApplicationUser = userLoginMapper.dtoToEntity(testUser, null);
        testApplicationUser.setCurrGroup(testGroup);
        userRepository.saveAndFlush(testApplicationUser);

        unitOfQuantityDataGenerator.generateUnitOfQuantity();
        List<UnitOfQuantity> quantities = unitOfQuantityRepository.findAll();
        Map<String, UnitOfQuantity> mappedUnits = new HashMap<>();
        for (UnitOfQuantity unit :
            quantities) {
            mappedUnits.put(unit.getName(), unit);
        }
        HashSet<ItemStorage> ingredients = new HashSet<ItemStorage>();
        ingredients.add(new ItemStorage("testItem", "item for tests", null, null,
            10, null, mappedUnits.get("g"), null, null));
        Recipe recipe = new Recipe(-1L, "testRecipe", "recipe for tests", ingredients, null, null);
        recipeRepository.saveAndFlush(recipe);
    }


}
