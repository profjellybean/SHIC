package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserLoginMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import at.ac.tuwien.sepm.groupphase.backend.entity.UserGroup;
import at.ac.tuwien.sepm.groupphase.backend.repository.CustomUserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListRepository;
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

    public UserDataGenerator(CustomUserRepository userRepository, UserLoginMapper userLoginMapper, ShoppingListRepository shoppingListRepository, ItemRepository itemRepository, UserGroupRepository userGroupRepository) {
        this.userRepository = userRepository;
        this.shoppingListRepository = shoppingListRepository;
        this.userLoginMapper = userLoginMapper;
        this.itemRepository = itemRepository;
        this.userGroupRepository = userGroupRepository;
    }

    @PostConstruct
    void generateUser() {



        Item item = new Item(null, "Döner", null);
        Long itemId = itemRepository.saveAndFlush(new Item(null, "Döner", null)).getId();
        UserRegistrationDto user = new UserRegistrationDto("user", "password", "user@email.com");

        Set<Item> items = new HashSet<>();
        items.add(itemRepository.getById(itemId));
        // Long shoppingListId = shoppingListRepository.saveAndFlush(   ShoppingList.ShoppingListBuilder.aShoppingList().withName("Your private shopping list").withItems(items).build()  ).getId();
        Long shoppingListId = shoppingListRepository.saveAndFlush(ShoppingList.ShoppingListBuilder.aShoppingList().withName("Your private shopping list").build()).getId();
        Optional<ApplicationUser> applicationUser = userRepository.findUserByUsername(user.getUsername());
        if (applicationUser.isEmpty()) {
            UserGroup group = new UserGroup();
            group = userGroupRepository.saveAndFlush(group);
            ApplicationUser u = userLoginMapper.dtoToEntity(user, shoppingListId);
            u.setCurrGroup(group);
            userRepository.saveAndFlush(u);
        }

        UserRegistrationDto admin = new UserRegistrationDto("admin", "password", "admin@email.com");
        shoppingListId = shoppingListRepository.saveAndFlush(ShoppingList.ShoppingListBuilder.aShoppingList().withName("Your private shopping list").build()).getId();
        applicationUser = userRepository.findUserByUsername(admin.getUsername());
        if (applicationUser.isEmpty()) {
            UserGroup group = new UserGroup();
            group = userGroupRepository.saveAndFlush(group);
            ApplicationUser u = userLoginMapper.dtoToEntity(admin, shoppingListId);
            u.setCurrGroup(group);
            userRepository.saveAndFlush(u);
        }

    }
}
