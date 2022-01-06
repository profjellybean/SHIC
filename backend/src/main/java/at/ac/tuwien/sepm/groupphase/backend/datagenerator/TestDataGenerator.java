package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ItemStorageMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserLoginMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.Register;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import at.ac.tuwien.sepm.groupphase.backend.entity.UserGroup;
import at.ac.tuwien.sepm.groupphase.backend.repository.BillRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemStorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RegisterRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UnitOfQuantityRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserGroupRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.HashSet;
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
     * generates Data used in Tests for the method
     * planRecipe(Long recipeId, Authentication auth) method
     * in ShoppingListService.
     */
    public void generateData_planRecipe() {
        LOGGER.debug("Generating Data for planning Recipe");
        itemStorageDataGenerator.generateItemStorage(); // includes UnitOfQuantity and Storage
        recipeDataGenerator.generateRecipes(); // includes UnitOfQuantity
        userDataGenerator.generateUser(); // includes ShoppingList and Storage
    }

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

        UserGroup testGroup = new UserGroup(null, null, -1L, new HashSet<ApplicationUser>());
        testGroup = userGroupRepository.saveAndFlush(testGroup);

        ApplicationUser testApplicationUser = userLoginMapper.dtoToEntity(testUser, null);
        testApplicationUser.setCurrGroup(testGroup);
        userRepository.saveAndFlush(testApplicationUser);

        Set<ApplicationUser> users = testGroup.getUser();
        users.add(testApplicationUser);
        testGroup.setUser(users);
        userGroupRepository.saveAndFlush(testGroup);

    }

    public void generateData_billSumOfCurrentMonth_noBills() {
        LOGGER.debug("Generating Data for sum of Bills in current month (no Bills)");

        UserRegistrationDto testUser = new UserRegistrationDto("testUser", "password", "test.user@email.com");

        UserGroup testGroup = new UserGroup(null, null, -1L, new HashSet<ApplicationUser>());
        testGroup = userGroupRepository.saveAndFlush(testGroup);

        ApplicationUser testApplicationUser = userLoginMapper.dtoToEntity(testUser, null);
        testApplicationUser.setCurrGroup(testGroup);
        userRepository.saveAndFlush(testApplicationUser);

        Set<ApplicationUser> users = testGroup.getUser();
        users.add(testApplicationUser);
        testGroup.setUser(users);
        userGroupRepository.saveAndFlush(testGroup);

    }

    public Long generateData_editMonthlyBudget_inRegister() {
        LOGGER.debug("Generating Data for editing Monthly Budget");

        UserRegistrationDto testUser = new UserRegistrationDto("testUser", "password", "test.user@email.com");

        Register register = new Register(-1L, null, 0.0, 200.0);
        register = registerRepository.saveAndFlush(register);

        UserGroup testGroup = new UserGroup(null, null, register.getId(), new HashSet<ApplicationUser>());
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

        UserGroup testGroup = new UserGroup(null, null, null, new HashSet<ApplicationUser>());
        testGroup = userGroupRepository.saveAndFlush(testGroup);

        ApplicationUser testApplicationUser = userLoginMapper.dtoToEntity(testUser, null);
        testApplicationUser.setCurrGroup(testGroup);
        userRepository.saveAndFlush(testApplicationUser);

        Set<ApplicationUser> users = testGroup.getUser();
        users.add(testApplicationUser);
        testGroup.setUser(users);
        userGroupRepository.saveAndFlush(testGroup);
    }


}
