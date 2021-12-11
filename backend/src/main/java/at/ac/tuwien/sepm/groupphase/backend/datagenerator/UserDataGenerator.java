package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserLoginMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import at.ac.tuwien.sepm.groupphase.backend.repository.CustomUserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListRepository;
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

    public UserDataGenerator(CustomUserRepository userRepository, UserLoginMapper userLoginMapper, ShoppingListRepository shoppingListRepository, ItemRepository itemRepository) {
        this.userRepository = userRepository;
        this.shoppingListRepository = shoppingListRepository;
        this.userLoginMapper = userLoginMapper;
        this.itemRepository = itemRepository;
    }

    @PostConstruct
    void generateUser() {


        Item item = new Item(null, "Döner", null);
        Long itemId = itemRepository.saveAndFlush(new Item(null, "Döner", null)).getId();
        Set<Item> items = new HashSet<>();
        items.add(itemRepository.getById(itemId));
        UserLoginDto user = new UserLoginDto("user@email.com", "password");
        UserLoginDto admin = new UserLoginDto("admin@email.com", "password");
        // Long shoppingListId = shoppingListRepository.saveAndFlush(   ShoppingList.ShoppingListBuilder.aShoppingList().withName("Your private shopping list").withItems(items).build()  ).getId();
        Long shoppingListId = shoppingListRepository.saveAndFlush(ShoppingList.ShoppingListBuilder.aShoppingList().withName("Your private shopping list").build()).getId();
        Optional<ApplicationUser> applicationUser = userRepository.findUserByUsername(user.getUsername());
        if (applicationUser.isEmpty()) {
            userRepository.save(userLoginMapper.dtoToEntity(user, shoppingListId));
        }

        shoppingListId = shoppingListRepository.saveAndFlush(ShoppingList.ShoppingListBuilder.aShoppingList().withName("Your private shopping list").build()).getId();
        applicationUser = userRepository.findUserByUsername(admin.getUsername());
        if (applicationUser.isEmpty()) {
            userRepository.save(userLoginMapper.dtoToEntity(admin, shoppingListId));
        }


    }
}
