package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserLoginMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import at.ac.tuwien.sepm.groupphase.backend.entity.Storage;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.entity.UserGroup;
import at.ac.tuwien.sepm.groupphase.backend.repository.CustomUserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RegisterRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.StorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UnitOfQuantityRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class UserDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    private final CustomUserRepository userRepository;
    private final ShoppingListRepository shoppingListRepository;
    private final ItemRepository itemRepository;
    private final UnitOfQuantityDataGenerator unitOfQuantityDataGenerator;
    private final UnitOfQuantityRepository unitOfQuantityRepository;
    private final UserLoginMapper userLoginMapper;
    private final UserGroupRepository userGroupRepository;
    private final StorageRepository storageRepository;
    private final StorageDataGenerator storageDataGenerator;
    private final RegisterRepository registerRepository;

    public UserDataGenerator(CustomUserRepository userRepository, UserLoginMapper userLoginMapper,
                             ShoppingListRepository shoppingListRepository, ItemRepository itemRepository,
                             UserGroupRepository userGroupRepository, StorageRepository storageRepository,
                             StorageDataGenerator storageDataGenerator, RegisterRepository registerRepository,
                             UnitOfQuantityDataGenerator unitOfQuantityDataGenerator, UnitOfQuantityRepository unitOfQuantityRepository) {
        this.userRepository = userRepository;
        this.shoppingListRepository = shoppingListRepository;
        this.userLoginMapper = userLoginMapper;
        this.itemRepository = itemRepository;
        this.userGroupRepository = userGroupRepository;
        this.storageRepository = storageRepository;
        this.storageDataGenerator = storageDataGenerator;
        this.registerRepository = registerRepository;
        this.unitOfQuantityDataGenerator = unitOfQuantityDataGenerator;
        this.unitOfQuantityRepository = unitOfQuantityRepository;
    }

    @PostConstruct
    void generateUser() throws IOException {
        LOGGER.debug("generating Data for User");
        storageDataGenerator.generateStorage();

        UserGroup group;
        UserRegistrationDto user = new UserRegistrationDto("Heidi", "password", "user@email.com");

        Set<Item> items = new HashSet<>();
        Long shoppingListId = shoppingListRepository.saveAndFlush(ShoppingList.ShoppingListBuilder.aShoppingList().withName("Your private shopping list").build()).getId();
        Optional<ApplicationUser> applicationUser = userRepository.findUserByUsername(user.getUsername());
        if (userGroupRepository.findAll().isEmpty()) {
            Long publicShoppingListId = shoppingListRepository.saveAndFlush(new ShoppingList()).getId();
            Long publicStorageId = storageRepository.saveAndFlush(new Storage()).getId();
            group = new UserGroup(publicStorageId, publicShoppingListId, 1L, new HashSet<ApplicationUser>(), "WG-Wipplingerstraße");
            group = userGroupRepository.saveAndFlush(group);
            Set<ApplicationUser> users = group.getUser();
            if (applicationUser.isEmpty()) {
                ApplicationUser u = userLoginMapper.dtoToEntity(user, shoppingListId);
                u.setCurrGroup(group);
                u.setImage(Files.readAllBytes(Paths.get("heidi.png")));
                userRepository.saveAndFlush(u);
                users.add(u);
                group.setUser(users);
                userGroupRepository.saveAndFlush(group);
            }
            UserRegistrationDto admin = new UserRegistrationDto("Leopold", "password", "admin@email.com");
            shoppingListId = shoppingListRepository.saveAndFlush(ShoppingList.ShoppingListBuilder.aShoppingList().withName("Your private shopping list").build()).getId();
            applicationUser = userRepository.findUserByUsername(admin.getUsername());
            if (applicationUser.isEmpty()) {
                ApplicationUser u = userLoginMapper.dtoToEntity(admin, shoppingListId);
                u.setCurrGroup(group);
                u.setImage(Files.readAllBytes(Paths.get("leopold.png")));
                userRepository.saveAndFlush(u);
                users = userGroupRepository.findAll().get(0).getUser();
                users.add(u);
                group.setUser(users);
                userGroupRepository.saveAndFlush(group);
            }
            generateItemsForGroup(group.getId());
        }
    }

    private void generateItemsForGroup(Long id) {
        LOGGER.debug("generating Items for Group");

        if (id == null) {
            return;
        }

        unitOfQuantityDataGenerator.generateUnitOfQuantity();
        List<UnitOfQuantity> unitList = unitOfQuantityRepository.findAll();
        Map<String, UnitOfQuantity> mappedUnits = new HashMap<>();
        for (UnitOfQuantity unit :
            unitList) {
            mappedUnits.put(unit.getName(), unit);
        }

        Item item1 = new Item(null, "Avocado", mappedUnits.get("pieces"), id);
        Item item2 = new Item(null, "NicNacs", mappedUnits.get("kg"), id);
        Item item3 = new Item(null, "Stiegl", mappedUnits.get("L"), id);
        Item item4 = new Item(null, "Orange Juice", mappedUnits.get("L"), id);
        List<Item> itemList = new ArrayList<Item>() {
            {
                add(item1);
                add(item2);
                add(item3);
                add(item4);
            }
        };
        itemRepository.saveAllAndFlush(itemList);
    }

    void generateApplicationUser() {
        Long shoppingListIdUser = shoppingListRepository.saveAndFlush(ShoppingList.ShoppingListBuilder.aShoppingList().withName("Your private shopping list").build()).getId();
        Long shoppingListIdAdmin = shoppingListRepository.saveAndFlush(ShoppingList.ShoppingListBuilder.aShoppingList().withName("Your private shopping list").build()).getId();
        UserGroup userGroup = new UserGroup();
        userGroup = userGroupRepository.saveAndFlush(userGroup);
        ApplicationUser user = new ApplicationUser("user@email.com", "password", userGroup, shoppingListIdUser);
        ApplicationUser admin = new ApplicationUser("admin@email.com", "password", userGroup, shoppingListIdAdmin);
        userRepository.saveAndFlush(user);
        userRepository.saveAndFlush(admin);

        /*
        Set<ApplicationUser> users = userGroup.getUser();
        users.add(user);
        users.add(admin);
        userGroup.setUser(users);
        userGroupRepository.saveAndFlush(userGroup);*/
    }
}

