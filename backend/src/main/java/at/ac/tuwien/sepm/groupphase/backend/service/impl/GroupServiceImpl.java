package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Register;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import at.ac.tuwien.sepm.groupphase.backend.entity.Storage;
import at.ac.tuwien.sepm.groupphase.backend.entity.UserGroup;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.repository.RegisterRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.StorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserGroupRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.Set;

@Service
public class GroupServiceImpl implements GroupService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserGroupRepository userGroupRepository;
    private final StorageRepository storageRepository;
    private final ShoppingListRepository shoppingListRepository;
    private final UserRepository userRepository;
    private final RegisterRepository registerRepository;

    public GroupServiceImpl(UserGroupRepository userGroupRepository, StorageRepository storageRepository, ShoppingListRepository shoppingListRepository, UserRepository userRepository, RegisterRepository registerRepository) {
        this.userGroupRepository = userGroupRepository;
        this.storageRepository = storageRepository;
        this.shoppingListRepository = shoppingListRepository;
        this.userRepository = userRepository;
        this.registerRepository = registerRepository;
    }


    @Override
    public Long generateUserGroup(String groupName, String username) {
        LOGGER.debug("Generate new group");
        UserGroup group = new UserGroup(groupName, this.storageRepository.saveAndFlush(new Storage()).getId(),
            this.shoppingListRepository.saveAndFlush(new ShoppingList()).getId(), this.registerRepository.saveAndFlush(new Register()).getId());
        Long ret = this.userGroupRepository.saveAndFlush(group).getId();
        if (username != null) {
            addUser(ret, username);
        }
        return ret;
    }

    @Override
    public void addUser(Long groupId, String username) {
        LOGGER.debug("Add user {} to group {}", username, groupId);
        UserGroup userGroup = this.userGroupRepository.getById(groupId);
        Set<ApplicationUser> user = userGroup.getUser();
        for (ApplicationUser u : user) {
            if (u.getUsername().equals(username)) {
                System.out.println("IN HERE");
                throw new ServiceException("The User " + username + " is already in the group.");
            }
        }
        Optional<ApplicationUser> temp = this.userRepository.findUserByUsername(username);
        if (temp.isPresent()) {
            if (temp.get().getCurrGroup() == null) {
                userGroup.setUser(user);
                userGroup = this.userGroupRepository.saveAndFlush(userGroup);
                temp.get().setCurrGroup(userGroup);
                this.userRepository.saveAndFlush(temp.get());
                Set<ApplicationUser> users = userGroup.getUser();
                users.add(temp.get());
                userGroup.setUser(users);
                this.userGroupRepository.saveAndFlush(userGroup);
            } else {
                throw new ServiceException("This user is already in a group");
            }
        } else {
            throw new NotFoundException("User " + username + " was not found!");
        }
    }

    @Override
    public Set<ApplicationUser> getAllUsers(Long groupId) {
        LOGGER.debug("Get all users from group {}", groupId);
        Optional<UserGroup> group = this.userGroupRepository.findById(groupId);
        if (group.isPresent()) {
            return group.get().getUser();
        } else {
            throw new NotFoundException("Group doesn't exist");
        }
    }

    @Override
    public UserGroup getOneById(Long id) {
        LOGGER.debug("Service: Get one group by id {}", id);
        Optional<UserGroup> group = this.userGroupRepository.findById(id);
        if (group.isPresent()) {
            return group.get();
        } else {
            throw new NotFoundException("Group doesn't exist");
        }
    }
}
