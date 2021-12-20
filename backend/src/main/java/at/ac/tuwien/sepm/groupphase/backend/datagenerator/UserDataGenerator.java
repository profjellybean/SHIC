package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserLoginMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import at.ac.tuwien.sepm.groupphase.backend.entity.Storage;
import at.ac.tuwien.sepm.groupphase.backend.entity.UserGroup;
import at.ac.tuwien.sepm.groupphase.backend.repository.CustomUserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.StorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

//@Profile("generateData")
@Component
public class UserDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    private final CustomUserRepository userRepository;
    private final ShoppingListRepository shoppingListRepository;
    private final ItemRepository itemRepository;
    private final UserLoginMapper userLoginMapper;
    private final UserGroupRepository userGroupRepository;
    private final StorageRepository storageRepository;

    public UserDataGenerator(CustomUserRepository userRepository, UserLoginMapper userLoginMapper,
                             ShoppingListRepository shoppingListRepository, ItemRepository itemRepository, UserGroupRepository userGroupRepository, StorageRepository storageRepository) {
        this.userRepository = userRepository;
        this.shoppingListRepository = shoppingListRepository;
        this.userLoginMapper = userLoginMapper;
        this.itemRepository = itemRepository;
        this.userGroupRepository = userGroupRepository;
        this.storageRepository = storageRepository;
    }

    @PostConstruct
    void generateUser() { //TODO remove

        UserGroup group = null;
        Item item = new Item(null, "Döner", null);
        Long itemId = itemRepository.saveAndFlush(new Item(null, "Döner", null)).getId();
        UserRegistrationDto user = new UserRegistrationDto("user", "password", "user@email.com");

        Set<Item> items = new HashSet<>();
        items.add(itemRepository.getById(itemId));
        // Long shoppingListId = shoppingListRepository.saveAndFlush(   ShoppingList.ShoppingListBuilder.aShoppingList().withName("Your private shopping list").withItems(items).build()  ).getId();
        Long shoppingListId = shoppingListRepository.saveAndFlush(ShoppingList.ShoppingListBuilder.aShoppingList().withName("Your private shopping list").build()).getId();
        Optional<ApplicationUser> applicationUser = userRepository.findUserByUsername(user.getUsername());
        if (applicationUser.isEmpty()) {
            Long publicShoppingListId = shoppingListRepository.saveAndFlush(new ShoppingList()).getId();
            Long publicStorageId = storageRepository.saveAndFlush(new Storage()).getId();
            group = new UserGroup(publicStorageId, publicShoppingListId);
            group = userGroupRepository.saveAndFlush(group);
            ApplicationUser u = userLoginMapper.dtoToEntity(user, shoppingListId);
            u.setCurrGroup(group);
            userRepository.saveAndFlush(u);

        }
        UserRegistrationDto admin = new UserRegistrationDto("admin", "password", "admin@email.com");
        shoppingListId = shoppingListRepository.saveAndFlush(ShoppingList.ShoppingListBuilder.aShoppingList().withName("Your private shopping list").build()).getId();
        applicationUser = userRepository.findUserByUsername(admin.getUsername());
        if (applicationUser.isEmpty()) {
            Long publicShoppingListId = shoppingListRepository.saveAndFlush(new ShoppingList()).getId();
            Long publicStorageId = storageRepository.saveAndFlush(new Storage()).getId();
            //UserGroup group = new UserGroup(publicStorageId, publicShoppingListId);
            //group = userGroupRepository.saveAndFlush(group);
            ApplicationUser u = userLoginMapper.dtoToEntity(admin, shoppingListId);
            u.setCurrGroup(group);
            userRepository.saveAndFlush(u);
            group = null;
        }

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

