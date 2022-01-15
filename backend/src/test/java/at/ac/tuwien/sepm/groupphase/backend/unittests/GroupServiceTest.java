package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserGroupRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.GroupServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class GroupServiceTest {
    @Autowired
    private GroupServiceImpl groupService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserGroupRepository userGroupRepository;
    @Autowired
    private ShoppingListRepository shoppingListRepository;

    @Test
    public void generateNewUserGroup() {
        long id = groupService.generateUserGroup(null, null);
        assertTrue(userGroupRepository.findById(id).isPresent());
    }

    @Test
    public void addingUserToGroupWhenAlreadyInGroupShouldThrowServiceException() {
        long id = groupService.generateUserGroup(null, null);
        Long shoppingListId = shoppingListRepository.saveAndFlush(ShoppingList.ShoppingListBuilder.aShoppingList().withName("Your private shopping list").build()).getId();
        ApplicationUser user = new ApplicationUser("Name", "12345678", shoppingListId, "name@email.com");
        userRepository.saveAndFlush(user);

        groupService.addUser(id, user.getUsername());
        assertThrows(ServiceException.class, () -> groupService.addUser(id, user.getUsername()));
    }

    @Test
    public void addingUserThatNotExistsToGroupShouldThrowNotFoundException() {
        long id = groupService.generateUserGroup(null, null);
        assertThrows(NotFoundException.class, () -> groupService.addUser(id, "Not a user"));
    }
}
